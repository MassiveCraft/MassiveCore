package com.massivecraft.mcore.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

import net.minecraft.server.v1_7_R3.DedicatedServer;
import net.minecraft.server.v1_7_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_7_R3.NBTTagCompound;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_7_R3.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.event.MCorePlayerLeaveEvent;
import com.massivecraft.mcore.event.MCoreSenderRegisterEvent;
import com.massivecraft.mcore.event.MCoreSenderUnregisterEvent;
import com.massivecraft.mcore.fetcher.Fetcher;
import com.massivecraft.mcore.fetcher.IdAndName;
import com.massivecraft.mcore.mixin.Mixin;
import com.massivecraft.mcore.store.Coll;
import com.massivecraft.mcore.store.SenderColl;
import com.massivecraft.mcore.store.SenderEntity;
import com.massivecraft.mcore.xlib.gson.reflect.TypeToken;

/**
 * Identification of a CommandSender can be done in 4 different ways.
 * 
 * 1. CommandSender sender (the sender itself)
 * 2. UUID senderUuid (the uuid for the sender)
 * 3. String senderId (the string id for the sender)
 * 4. String senderName (the name for the sender)
 * (5). Object senderObject (any of the four above) 
 * 
 * This works very fine for players (instances of the Player class).
 * The UUID would be the Mojang account uuid, the id would be the stringified UUID and the name would be the current player name (it's always unique even though it can be changed).
 * 
 * Then there is the server console command sender (instance of ConsoleCommandSender).
 * This one does not natively have a proper uuid, id or name.
 * I have however given it some.
 * It's very often practical being able to treat the console as any player.
 * 
 * We provide the following features:
 * - Lookup of all the data based on one of the unique parts.
 * - Maintained sets for All, Online and Offline.
 * 
 * Players are registered automatically.
 * Console is registered with imaginary and deterministic data values.
 * Non standard CommandSenders must be manually registered using the register method.
 */
public class IdUtil implements Listener, Runnable
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	// This class mostly contains static content.
	// A few things needs an instantiated class though.
	// Such as the event listeners.
	
	private static IdUtil i = new IdUtil();
	public static IdUtil get() { return i; }
	
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	// Taken directly from my own imagination!
	
	public final static String IDPREFIX = "@";
	public final static String CONSOLE_ID = IDPREFIX+"console";
	public final static IdData CONSOLE_DATA = new IdData(CONSOLE_ID, CONSOLE_ID);
	
	// This is lock object is used when reading from and saving to this file.
	// Useful since saving might happen async.
	private final static Object CACHEFILE_LOCK = new Object();
	public final static File CACHEFILE = new File(MCore.get().getDataFolder(), "idnamecache.json");
	public final static File CACHEFILE_TEMP = new File(MCore.get().getDataFolder(), "idnamecache.json.temp");
	public final static Type CACHEFILE_TYPE = new TypeToken<Set<IdData>>() {}.getType();
	
	// -------------------------------------------- //
	// DATA STORAGE
	// -------------------------------------------- //
	// IdData storage. Maintaining relation between name and id.
	
	// The full set
	private static Set<IdData> datas = Collections.newSetFromMap(new ConcurrentHashMap<IdData, Boolean>());
	public static Set<IdData> getDatas() { return datas; }
	
	// Id Index
	private static Map<String, IdData> idToData = new ConcurrentSkipListMap<>(String.CASE_INSENSITIVE_ORDER);
	public static Map<String, IdData> getIdToData() { return idToData;  }
	
	// Name Index
	private static Map<String, IdData> nameToData = new ConcurrentSkipListMap<>(String.CASE_INSENSITIVE_ORDER);
	public static Map<String, IdData> getNameToData() { return nameToData; }
	
	// -------------------------------------------- //
	// MAINTAINED SETS
	// -------------------------------------------- //
	// Used for chat tab completion, argument readers, etc.
	
	private static Set<String> onlineIds = new ConcurrentSkipListSet<>(String.CASE_INSENSITIVE_ORDER);
	public static Set<String> getOnlineIds() { return Collections.unmodifiableSet(onlineIds); }
	
	private static Set<String> onlineNames = new ConcurrentSkipListSet<>(String.CASE_INSENSITIVE_ORDER);
	public static Set<String> getOnlineNames() { return Collections.unmodifiableSet(onlineNames); }
	
	
	private static Set<String> allIds = new ConcurrentSkipListSet<>(String.CASE_INSENSITIVE_ORDER);
	public static Set<String> getAllIds() { return Collections.unmodifiableSet(allIds); }
	
	private static Set<String> allNames = new ConcurrentSkipListSet<>(String.CASE_INSENSITIVE_ORDER);
	public static Set<String> getAllNames() { return Collections.unmodifiableSet(allNames); }
	
	// -------------------------------------------- //
	// REGISTRY
	// -------------------------------------------- //
	// For registering extra custom CommandSender implementations.
	// It's assumed that the getName() returns the name which is also the id.
	
	private static Map<String, CommandSender> registryIdToSender = new ConcurrentHashMap<>();
	private static Map<CommandSender, String> registrySenderToId = new ConcurrentHashMap<>();
	
	public static void register(CommandSender sender)
	{
		if (sender == null) throw new NullPointerException("sender");
		String id = getId(sender);
		String name = id;
		IdData data = new IdData(id, name);
		
		registryIdToSender.put(id, sender);
		registrySenderToId.put(sender, id);
		
		// Update data before the event is ran so that data is available.
		update(id, name, true);
		
		MCoreSenderRegisterEvent event = new MCoreSenderRegisterEvent(sender, data);
		event.run();
	}
	
	public static void unregister(CommandSender sender)
	{
		if (sender == null) throw new NullPointerException("sender");
		String id = getId(sender);
		String name = id;
		IdData data = new IdData(id, name);
		
		Object removed = registryIdToSender.remove(id);
		registrySenderToId.remove(sender);
		if (removed == null) return;
		
		// Update data before the event is ran so that data is available.
		update(id, name, false);
		
		MCoreSenderUnregisterEvent event = new MCoreSenderUnregisterEvent(sender, data);
		event.run();
	}
	
	// -------------------------------------------- //
	// GET ONLINE SENDERS
	// -------------------------------------------- //
	// Used for retrieving the full set of senders currently present on this server.
	
	public static Set<CommandSender> getOnlineSenders()
	{
		Set<CommandSender> ret = new LinkedHashSet<CommandSender>();
		
		// Add Online Players
		ret.addAll(Arrays.asList(Bukkit.getOnlinePlayers()));
		
		// Add Console
		ret.add(getConsole());
		
		// Add Registered
		ret.addAll(registryIdToSender.values());
		
		return ret;
	}
	
	// -------------------------------------------- //
	// UPDATE ONE
	// -------------------------------------------- //
	
	// Returns the name corresponding to the removed id.
	private static String removeId(String id)
	{
		if (id == null) throw new NullPointerException("id");
		
		onlineIds.remove(id);
		allIds.remove(id);
		
		IdData data = idToData.remove(id);
		if (data == null) return null;
		datas.remove(data);
		return data.getName();
	}
	
	// Returns the id corresponding to the removed name.
	private static String removeName(String name)
	{
		if (name == null) throw new NullPointerException("name");
		
		onlineNames.remove(name);
		allNames.remove(name);
		
		IdData data = nameToData.remove(name);
		if (data == null) return null;
		datas.remove(data);
		return data.getId();
	}
	
	private static void addData(IdData data, boolean online)
	{
		if (data == null) throw new NullPointerException("data");
		String id = data.getId();
		String name = data.getName();
		
		datas.add(data);
		
		if (id != null)
		{
			idToData.put(id, data);
		}
		
		if (name != null)
		{
			nameToData.put(name, data);
		}
		
		if (id != null && name != null)
		{
			if (online) onlineIds.add(id);
			allIds.add(id);
			
			if (online) onlineNames.add(name);
			allNames.add(name);
		}
	}
	
	public static void update(String id, String name)
	{
		update(id, name, null);
	}
	
	public static void update(String id, String name, long millis)
	{
		update(id, name, millis, null);
	}
	
	public static void update(final String id, final String name, Boolean online)
	{
		update(id, name, System.currentTimeMillis(), online);
	}
	
	public static void update(final String id, final String name, final long millis, Boolean online)
	{
		// First Null Check
		if (id == null && name == null) throw new NullPointerException("Either id or name must be set. They can't both be null.");
		
		// Calculate previous millis
		// The millis must be newer than the previous millis for the update to continue.
		Long previousMillis = null;
		Long idMillis = getMillis(id);
		if (idMillis != null && (previousMillis == null || idMillis < previousMillis))
		{
			previousMillis = idMillis;
		}
		Long nameMillis = getMillis(name);
		if (nameMillis != null && (previousMillis == null || nameMillis < previousMillis))
		{
			previousMillis = nameMillis;
		}
		// The previousMillis is now null or the lowest of the two.
		if (previousMillis != null && previousMillis > millis) return;
		
		// Online Fix
		if (online == null)
		{
			if (id != null)
			{
				online = onlineIds.contains(id);
			}
			else if (name != null)
			{
				online = onlineNames.contains(name);
			}
		}
		
		// Removal of previous data
		// TODO: This is not optimal but will do for now. It only "uproots" one step.
		String otherId = null;
		String otherName = null;
		
		if (id != null) otherName = removeId(id);
		if (name != null) otherId = removeName(name);
		
		if (otherId != null && !otherId.equals(id)) removeId(otherId);
		if (otherName != null && !otherName.equals(name)) removeName(otherName);
		
		// Adding new data
		IdData data = new IdData(id, name, millis);
		addData(data, online);
	}
	
	// -------------------------------------------- //
	// SETUP
	// -------------------------------------------- //
	// This is ran as the initial setup.
	// Do not call this manually.
	// It should only be called on plugin enable.

	@SuppressWarnings("deprecation")
	public static void setup()
	{
		// Time: Start
		long start = System.currentTimeMillis();
		
		// Clear Datas
		datas.clear();
		idToData.clear();
		nameToData.clear();
		
		onlineIds.clear();
		onlineNames.clear();
		
		allIds.clear();
		allNames.clear();
		
		// Load Datas
		loadDatas();
		
		// Since Console initially does not exist we schedule the register.
		Bukkit.getScheduler().scheduleSyncDelayedTask(MCore.get(), new Runnable()
		{
			@Override
			public void run()
			{
				register(getConsole());
			}
		});
		
		// Cachefile
		long ticks = 20*60; // 5min
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(MCore.get(), get(), ticks, ticks);
		
		// Register Event Listeners
		Bukkit.getPluginManager().registerEvents(get(), MCore.get());
		
		// Time: End
		long end = System.currentTimeMillis();
		MCore.get().log(Txt.parse("<i>Setup of IdUtil took <h>%d<i>ms.", end-start));
	}
	
	// -------------------------------------------- //
	// LISTENER
	// -------------------------------------------- //
	// Here we maintain the maps continuously.
	// Filling them on setup only would not work.
	// When players log on/off that must be taken note of.
	
	// We don't care if it's cancelled or not.
	// We just wan't to make sure this id is known of and can be "fixed" asap.
	// Online or not? We just use the mixin to get the actuall value.
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void playerLoginLowest(PlayerLoginEvent event)
	{
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		String id = uuid.toString();
		String name = player.getName();
		
		// Declaring Existence? Sure, whatever you were before!
		boolean online = Mixin.isOnline(player);
		
		update(id, name, online);
	}
	
	// Can't be cancelled
	@EventHandler(priority = EventPriority.LOWEST)
	public void playerJoinLowest(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		String id = uuid.toString();
		String name = player.getName();
		
		// Joining? Sure, the player is online!
		boolean online = true;
		
		update(id, name, online);
	}
	
	// Can't be cancelled
	@EventHandler(priority = EventPriority.HIGHEST)
	public void playerLeaveMonitor(MCorePlayerLeaveEvent event)
	{
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		String id = uuid.toString();
		String name = player.getName();
		
		// Leaving? Is it an actuall leave?
		boolean online = !Mixin.isActualLeave(event);
		
		update(id, name, online);
	}
	
	// -------------------------------------------- //
	// GET DATA
	// -------------------------------------------- //
	// Get data from a senderObject (any of: CommandSender, UUID, String id, String name)
	// These methods avoid using the data cache if possible.
	
	public static IdData getData(Object senderObject)
	{
		// Null Return
		if (senderObject == null) return null;
		
		// Already Done
		if (senderObject instanceof IdData) return (IdData) senderObject;
		
		// Console Type
		if (senderObject instanceof ConsoleCommandSender) return CONSOLE_DATA;
		
		// Console Id/Name
		if (CONSOLE_ID.equals(senderObject)) return CONSOLE_DATA;
		
		// SenderEntity Recurse
		if (senderObject instanceof SenderEntity<?>)
		{
			SenderEntity<?> senderEntity = (SenderEntity<?>)senderObject;
			return getData(senderEntity.getId());
		}
		
		// Player
		// CommandSender
		// UUID
		if (senderObject instanceof CommandSender || senderObject instanceof UUID)
		{
			String id = getId(senderObject);
			return getIdToData().get(id);
		}
		
		// String
		if (senderObject instanceof String)
		{
			IdData ret = getIdToData().get(senderObject);
			if (ret != null) return ret;
			return getNameToData().get(senderObject);
		}
		
		// Return Null
		return null;
	}
	
	public static ConsoleCommandSender getConsole()
	{
		return Bukkit.getConsoleSender();
	}
	
	public static Player getPlayer(Object senderObject)
	{
		return getAsPlayer(getSender(senderObject));
	}
	
	@SuppressWarnings("deprecation")
	public static CommandSender getSender(Object senderObject)
	{
		// Null Return
		if (senderObject == null) return null;
		
		// Already Done
		if (senderObject instanceof CommandSender) return (CommandSender) senderObject;
		
		// Console Type
		// Handled at "Already Done"
		
		// Console Id/Name
		if (CONSOLE_ID.equals(senderObject)) return getConsole();
		
		// SenderEntity Recurse
		if (senderObject instanceof SenderEntity<?>)
		{
			SenderEntity<?> senderEntity = (SenderEntity<?>)senderObject;
			return getSender(senderEntity.getId());
		}
		
		// Player
		// Handled at "Already Done"
		
		// CommandSender
		// Handled at "Already Done"
		
		// UUID
		if (senderObject instanceof UUID)
		{
			// Attempt finding player
			UUID uuid = (UUID)senderObject;
			Player player = Bukkit.getPlayer(uuid);
			if (player != null) return player;
			
			// Otherwise assume registered sender
			return registryIdToSender.get(uuid.toString());
		}
		
		// String
		if (senderObject instanceof String)
		{
			// Recurse as UUID
			String string = (String)senderObject;
			UUID uuid = uuidFromString(string);
			if (uuid != null) return getSender(uuid);
			
			// Registry
			CommandSender sender = registryIdToSender.get(string);
			if (sender != null) return sender;
			
			// Bukkit API
			return Bukkit.getPlayerExact(string);
		}
		
		// Return Null
		return null;
	}
	
	public static UUID getUuid(Object senderObject)
	{
		// Null Return
		if (senderObject == null) return null;
		
		// Already Done
		if (senderObject instanceof UUID) return (UUID)senderObject;
		
		// Console Type
		if (senderObject instanceof ConsoleCommandSender) return null;
		
		// Console Id/Name
		if (CONSOLE_ID.equals(senderObject)) return null;
		
		// SenderEntity Recurse
		if (senderObject instanceof SenderEntity<?>)
		{
			SenderEntity<?> senderEntity = (SenderEntity<?>)senderObject;
			return getUuid(senderEntity.getId());
		}
		
		// Player
		if (senderObject instanceof Player) return ((Player)senderObject).getUniqueId();
		
		// CommandSender
		if (senderObject instanceof CommandSender)
		{
			CommandSender sender = (CommandSender)senderObject;
			String id = sender.getName();
			return uuidFromString(id);
		}
		
		// UUID
		// Handled at "Already Done"
		
		// String
		if (senderObject instanceof String)
		{
			// Is UUID
			String string = (String)senderObject;
			UUID uuid = uuidFromString(string);
			if (uuid != null) return uuid;
			
			// Is Name
			// Handled at "Data"
		}
		
		// Data
		IdData data = getData(senderObject);
		if (data != null)
		{
			String id = data.getId();
			if (id == null) return null;
			UUID uuid = uuidFromString(id);
			return uuid;	
		}
		
		// Return Null
		return null;
	}

	public static String getId(Object senderObject)
	{
		// Null Return
		if (senderObject == null) return null;
		
		// Already Done
		if (senderObject instanceof String && MUtil.isValidUUID((String)senderObject)) return (String)senderObject;
		
		// Console Type
		if (senderObject instanceof ConsoleCommandSender) return CONSOLE_ID;
		
		// Console Id/Name
		if (CONSOLE_ID.equals(senderObject)) return CONSOLE_ID;
		
		// SenderEntity Recurse
		if (senderObject instanceof SenderEntity<?>)
		{
			SenderEntity<?> senderEntity = (SenderEntity<?>)senderObject;
			return getId(senderEntity.getId());
		}
		
		// Player
		if (senderObject instanceof Player) return ((Player)senderObject).getUniqueId().toString();
		
		// CommandSender
		if (senderObject instanceof CommandSender) return ((CommandSender)senderObject).getName();
		
		// UUID
		if (senderObject instanceof UUID) return ((UUID)senderObject).toString();
		
		// String
		// Handled at "Data"
		
		// Data
		IdData data = getData(senderObject);
		if (data != null)
		{
			return data.getId();
		}
		
		// Return Null
		return null;
	}

	public static String getName(Object senderObject)
	{
		// Null Return
		if (senderObject == null) return null;
		
		// Already Done
		// Handled at "Data" (not applicable - names can look differently)
		
		// Console Type
		if (senderObject instanceof ConsoleCommandSender) return CONSOLE_ID;
		
		// Console Id/Name
		if (CONSOLE_ID.equals(senderObject)) return CONSOLE_ID;
		
		// SenderEntity Recurse
		if (senderObject instanceof SenderEntity<?>)
		{
			SenderEntity<?> senderEntity = (SenderEntity<?>)senderObject;
			return getName(senderEntity.getId());
		}
		
		// Player
		// Handled at "CommandSender"
		
		// CommandSender
		if (senderObject instanceof CommandSender) return ((CommandSender)senderObject).getName();
		
		// UUID
		// Handled at "Data".
		
		// String
		// Handled at "Data"
		// Note: We try to use stored data to fix the capitalization!
		
		// Data
		IdData data = getData(senderObject);
		if (data != null)
		{
			return data.getName();
		}
		
		// TryFix Behavior
		// Note: We try to use stored data to fix the capitalization!
		if (senderObject instanceof String) return (String)senderObject;
		
		// Return Null
		return null;
	}
	
	public static boolean isOnline(Object senderObject)
	{
		// Fix the id ...
		if (senderObject == null) return false;
		String id = getId(senderObject);
		if (id == null) return false;
		
		// ... console is always online ...
		// (this counters console being offline till the first tick due to deferred register)
		if (CONSOLE_ID.equals(id)) return true;
		
		// ... return by (case insensitive) set contains.
		return getOnlineIds().contains(id);
	}
	
	public static Long getMillis(Object senderObject)
	{
		IdData data = getData(senderObject);
		if (data == null) return null;
		return data.getMillis();
	}

	// -------------------------------------------- //
	// ID TYPE CHECKING
	// -------------------------------------------- //
	
	public static boolean isPlayerId(String string)
	{
		// NOTE: Assuming all custom ids look like "@shite".
		return MUtil.isValidPlayerName(string) || MUtil.isValidUUID(string);
	}
	
	public static boolean isConsoleId(String string)
	{
		return CONSOLE_ID.equals(string);
	}
	
	// -------------------------------------------- //
	// GET AS
	// -------------------------------------------- //
	
	public static CommandSender getAsSender(Object object)
	{
		if (!(object instanceof CommandSender)) return null;
		return (CommandSender) object;
	}
	
	public static Player getAsPlayer(Object object)
	{
		if (!(object instanceof Player)) return null;
		return (Player) object;
	}
	
	public static ConsoleCommandSender getAsConsole(Object object)
	{
		if (!(object instanceof ConsoleCommandSender)) return null;
		return (ConsoleCommandSender) object;
	}
	
	// -------------------------------------------- //
	// CONVENIENCE GAME-MODE
	// -------------------------------------------- //
	
	public static GameMode getGameMode(Object object, GameMode def)
	{
		Player player = getPlayer(object);
		if (player == null) return def;
		return player.getGameMode();
	}

	public static boolean isGameMode(Object object, GameMode gm, boolean def)
	{
		Player player = getPlayer(object);
		if (player == null) return def;
		return player.getGameMode() == gm;
	}
	
	// -------------------------------------------- //
	// DATAS
	// -------------------------------------------- //
	
	public static void loadDatas()
	{
		MCore.get().log(Txt.parse("<i>Loading Playerdat datas..."));
		for (IdData data : getPlayerdatDatas())
		{
			update(data.getId(), data.getName(), data.getMillis(), false);
		}
		
		MCore.get().log(Txt.parse("<i>Loading Cachefile datas..."));
		for (IdData data : getCachefileDatas())
		{
			update(data.getId(), data.getName(), data.getMillis(), false);
		}
		
		MCore.get().log(Txt.parse("<i>Loading Onlineplayer datas..."));
		for (IdData data : getOnlineplayerDatas())
		{
			update(data.getId(), data.getName(), data.getMillis(), true);
		}
		
		MCore.get().log(Txt.parse("<i>Loading Registry datas..."));
		for (String id : registryIdToSender.keySet())
		{
			String name = id;
			update(id, name, true);
		}
		
		if (Bukkit.getServer().getOnlineMode())
		{
			MCore.get().log(Txt.parse("<i>Loading Dbmojangapi datas..."));
			for (IdData data : getDbmojangapiDatas())
			{
				update(data.getId(), data.getName(), data.getMillis());
			}
		}
		else
		{
			MCore.get().log(Txt.parse("<i>Skipping Dbmojangapi datas since offline mode..."));
		}
		
		
		MCore.get().log(Txt.parse("<i>Saving Cachefile..."));
		saveCachefileDatas();
	}
	
	// -------------------------------------------- //
	// CACHEFILE DATAS
	// -------------------------------------------- //
	
	public static void saveCachefileDatas()
	{
		synchronized (CACHEFILE_LOCK)
		{
			String content = MCore.gson.toJson(datas, CACHEFILE_TYPE);
			DiscUtil.writeCatch(CACHEFILE_TEMP, content);
			if (!CACHEFILE_TEMP.exists()) return;
			CACHEFILE.delete();
			CACHEFILE_TEMP.renameTo(CACHEFILE);
		}
	}
	
	public static Set<IdData> getCachefileDatas()
	{
		synchronized (CACHEFILE_LOCK)
		{
			String content = DiscUtil.readCatch(CACHEFILE);
			if (content == null) return new HashSet<IdData>();
			Set<IdData> ret = MCore.gson.fromJson(content, CACHEFILE_TYPE);
			return ret;
		}
	}
	
	@Override
	public void run()
	{
		saveCachefileDatas();
	}

	// -------------------------------------------- //
	// PLAYERDAT DATAS
	// -------------------------------------------- //
	// This data source is based on the player.dat files.
	// It extracts relevant NBT contents.
	// NOTE: The reason this section contains some NMS is because MCore loads at startup. Some of the Bukkit API is broken then and I could not use it.
	
	public static File getBaseworldDirectory()
	{
		CraftServer cserver = (CraftServer)Bukkit.getServer();
		DedicatedServer dserver = (DedicatedServer)cserver.getServer();
		String levelName = dserver.propertyManager.getString("level-name", "world");
		return new File(Bukkit.getWorldContainer(), levelName);
	}
	
	public static File getPlayerdatDirectory()
	{
		// after 1.7.8
		// a2cce16b-9494-45ff-b5ff-0362ca687d4e.dat (the uuid)
		return new File(getBaseworldDirectory(), "playerdata");
	}
	
	public static Set<IdData> getPlayerdatDatas()
	{
		Set<IdData> ret = new LinkedHashSet<IdData>();
		
		// Get the directory
		File directory = getPlayerdatDirectory();
		
		// List the files in the directory
		File[] files = directory.listFiles();
		
		// The directory may not exist
		if (files == null) return ret;
		
		// For each file
		for (File file : files)
		{
			IdData data = getPlayerdatData(file);
			if (data == null) continue;
			ret.add(data);
		}
		
		return ret;
	}
	
	// For the data to be interesting it must contain the bukkit.laskKnownName NBT entry.
	// It's especially nice if bukkit.lastPlayed also is present.
	// If bukkit.lastPlayed isn't present we will set lastPlayed millis to 0 in the data.
	// This will mean the data is considered quite uncertain since data entries are prioritized after their millis.
	public static IdData getPlayerdatData(File file)
	{
		// Get filename
		String filename = file.getName();
		
		// Ensure filename ends with ".dat"
		if (!filename.toLowerCase().endsWith(".dat")) return null;
		
		// Pick id through ".dat" removal
		String id = filename.substring(0, filename.length()-4);
		
		// Ensure file name was of the correct format
		UUID uuid = uuidFromString(id);
		if (uuid == null) return null;
		
		// Load Compound
		NBTTagCompound compound = loadTagCompound(file);
		if (compound == null) return null;
		
		// Get Compound --> bukkit
		if (!compound.hasKey("bukkit")) return null;
		NBTTagCompound bukkit = compound.getCompound("bukkit");
		if (bukkit == null) return null;
		
		// Get Compound --> bukkit --> lastKnownName
		if (!bukkit.hasKey("lastKnownName")) return null;
		String name = bukkit.getString("lastKnownName");
		if (name == null) return null;
		
		// Last Played?
		long lastPlayed = 0;
		if (bukkit.hasKey("lastPlayed"))
		{
			lastPlayed = bukkit.getLong("lastPlayed");
		}
		
		// Add to ret
		return new IdData(id, name, lastPlayed);
	}

	public static NBTTagCompound loadTagCompound(File file)
	{
		if (!file.exists()) return null;
		try
		{
			return NBTCompressedStreamTools.a((InputStream) (new FileInputStream(file)));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	// -------------------------------------------- //
	// ONLINEPLAYER DATAS
	// -------------------------------------------- //
	// This data source is simply based on the players currently online
	
	public static Set<IdData> getOnlineplayerDatas()
	{
		Set<IdData> ret = new LinkedHashSet<IdData>();
		
		long millis = System.currentTimeMillis();
		
		for (Player player : Bukkit.getOnlinePlayers())
		{
			String id = getId(player);
			if (id == null) throw new NullPointerException("id");
			
			String name = getName(player);
			if (name == null) throw new NullPointerException("name");
			
			IdData data = new IdData(id, name, millis);
			
			ret.add(data);
		}
		
		return ret;
	}
	
	// -------------------------------------------- //
	// DBMOJANGAPI DATAS
	// -------------------------------------------- //
	// This data source searches the database for player names and ids (strings).
	// It then discards the strings already present in IdUtil.
	// The renaming strings are queried through the Mojang API.
	
	public static Set<IdData> getDbmojangapiDatas()
	{
		Set<IdData> ret = new LinkedHashSet<IdData>();
		
		// Add valid names from DB
		Set<String> strings = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		for (Coll<?> coll : Coll.getInstances())
		{
			if (!(coll instanceof SenderColl<?>)) continue;
			for (String id : coll.getIds())
			{
				// https://github.com/Mojang/AccountsClient/issues/2
				// Would have been nice but no. Mojang did not have time to implement this feature for us :/
				//if (MUtil.isValidPlayerName(id) || MUtil.isValidUUID(id))
				if (MUtil.isValidPlayerName(id))
				{
					strings.add(id);					
				}
			}
		}
		MCore.get().log(Txt.parse("<k>Player Strings Found: <v>%d", strings.size()));
		
		// Remove Cached
		Iterator<String> iter = strings.iterator();
		int cached = 0;
		while (iter.hasNext())
		{
			String string = iter.next();
			if (getData(string) != null)
			{
				cached++;
				iter.remove();
			}
		}
		MCore.get().log(Txt.parse("<k>Player Strings Cached: <v>%d", cached));
		MCore.get().log(Txt.parse("<k>Player Strings Remaining: <v>%d", strings.size()));
		
		// Fetch
		MCore.get().log(Txt.parse("<i>Now fetching the remaining players from Mojang API ..."));
		Collection<IdAndName> idAndNames = null;
		try
		{
			idAndNames = Fetcher.fetch(strings);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		MCore.get().log(Txt.parse("<i> ... done!"));
		long millis = System.currentTimeMillis();
		
		// Add
		for (IdAndName idAndName : idAndNames)
		{
			String id = null;
			UUID uuid = idAndName.getId();
			if (uuid != null) id = uuid.toString();
			
			String name = idAndName.getName();
			
			ret.add(new IdData(id, name, millis));
		}
		
		return ret;
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static UUID uuidFromString(String string)
	{
		try
		{ 
			return UUID.fromString(string);
		}
		catch (IllegalArgumentException e)
		{
			return null;
		}
	}
	
}
