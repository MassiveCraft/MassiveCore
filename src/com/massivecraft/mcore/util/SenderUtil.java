package com.massivecraft.mcore.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.minecraft.server.v1_5_R1.MinecraftServer;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.craftbukkit.v1_5_R1.CraftServer;
import org.bukkit.entity.Player;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.event.MCoreSenderRegisterEvent;
import com.massivecraft.mcore.event.MCoreSenderUnregisterEvent;
import com.massivecraft.mcore.sender.FakeBlockCommandSender;

/**
 * We add an ID <--> CommandSender lookup feature.
 * Each player has an id which is the name of the player. Players are retrievable by id using Bukkit.getPlayerExact().
 * Other command senders have no true id. We make it so they have.
 * Non-player-sender-ids always start with and ampersand (@). This is to avoid clashes with regular player names.
 * The id is simply "@"+CommandSender.getName() with exception for the block command sender which we call "@block".
 * Non standard CommandSenders must be manually registered to the util using the register method.
 */
public class SenderUtil
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	// The id prefix
	public final static String IDPREFIX = "@";
	
	// Ids for standard-non-players
	public final static String ID_CONSOLE = IDPREFIX+"console";
	public final static String ID_RCON = IDPREFIX+"rcon";
	public final static String ID_BLOCK = IDPREFIX+"block";
	
	// Names for standard-non-players
	public final static String VANILLA_CONSOLE_NAME = "CONSOLE";
	public final static String VANILLA_RCON_NAME = "Rcon";
	public final static String VANILLA_BLOCK_NAME = "@";
	
	// -------------------------------------------- //
	// REGISTRY
	// -------------------------------------------- //
	
	protected static Map<String, CommandSender> idToSender = new TreeMap<String, CommandSender>(String.CASE_INSENSITIVE_ORDER);
	protected static Map<String, String> idToListName = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
	
	public static synchronized boolean register(CommandSender sender)
	{
		if (sender == null) return false;
		String id = getSenderId(sender);
		idToSender.put(id, sender);
		new MCoreSenderRegisterEvent(sender).run();
		return true;
	}
	
	public static synchronized boolean unregister(CommandSender sender)
	{
		boolean ret = (idToSender.remove(getSenderId(sender)) != null);
		if (ret)
		{
			new MCoreSenderUnregisterEvent(sender).run();
		}
		return ret;
	}
	
	public static Map<String, CommandSender> getIdToSender()
	{
		return Collections.unmodifiableMap(idToSender);
	}
	
	static
	{
		// Since the console and rcon does not exist we schedule the register for these senders.
		Bukkit.getScheduler().scheduleSyncDelayedTask(MCore.get(), new Runnable()
		{
			@Override
			public void run()
			{
				register(getConsole());
				register(getRcon());
				register(getBlock());
			}
		});
	}
	
	// -------------------------------------------- //
	// ID TYPE CHECKING
	// -------------------------------------------- //
	
	public static boolean isSenderId(Object o)
	{
		return o instanceof String;
	}
	
	public static boolean isPlayerId(Object o)
	{
		return MUtil.isValidPlayerName(o);
	}
	
	public static boolean isConsoleId(Object o)
	{
		return ID_CONSOLE.equals(o);
	}
	
	public static boolean isRconId(Object o)
	{
		return ID_RCON.equals(o);
	}
	
	public static boolean isBlockId(Object o)
	{
		return ID_BLOCK.equals(o);
	}
	
	public static boolean isNonplayerId(Object o)
	{
		if (!isSenderId(o)) return false;
		if (isPlayerId(o)) return false;
		return true;
	}
	
	public static boolean isStandardNonplayerId(Object o)
	{
		if (isConsoleId(o)) return true;
		if (isRconId(o)) return true;
		if (isBlockId(o)) return true;
		return false;
	}
	
	public static boolean isNonstandardNonplayerId(Object o)
	{
		if (!isSenderId(o)) return false;
		if (isStandardNonplayerId(o)) return false;
		if (isPlayerId(o)) return false;
		return true;
	}
	
	// -------------------------------------------- //
	// SENDER/OBJECT TYPE CHECKING
	// -------------------------------------------- //
	
	public static boolean isSender(Object o)
	{
		// The object must be a CommandSender and musn't be null.
		return o instanceof CommandSender;
	}
	
	public static boolean isPlayer(Object o)
	{
		return o instanceof Player;
	}
	
	public static boolean isConsole(Object o)
	{
		if (!(o instanceof ConsoleCommandSender)) return false;
		if (!VANILLA_CONSOLE_NAME.equals(((CommandSender)o).getName())) return false;
		return true;
	}
	
	public static boolean isRcon(Object o)
	{
		if (!(o instanceof RemoteConsoleCommandSender)) return false;
		if (!VANILLA_RCON_NAME.equals(((CommandSender)o).getName())) return false;
		return true;
	}
	
	public static boolean isBlock(Object o)
	{
		if (!(o instanceof BlockCommandSender)) return false;
		if (!VANILLA_BLOCK_NAME.equals(((CommandSender)o).getName())) return false;
		return true;
	}
	
	public static boolean isNonplayer(Object o)
	{
		if (!isSender(o)) return false;
		if (isPlayer(o)) return false;
		return true;
	}
	
	public static boolean isStandardNonplayer(Object o)
	{
		if (isConsole(o)) return true;
		if (isRcon(o)) return true;
		if (isBlock(o)) return true;
		return false;
	}
	
	public static boolean isNonstandardNonplayer(Object o)
	{
		if (!isSender(o)) return false;
		if (isStandardNonplayer(o)) return false;
		if (isPlayer(o)) return false;
		return true;
	}
	
	// -------------------------------------------- //
	// GET ID
	// -------------------------------------------- //
	
	public static String getSenderId(Object o)
	{
		if (!isSender(o)) return null;
		if (isPlayer(o)) return ((CommandSender)o).getName();
		if (isConsole(o)) return ID_CONSOLE;
		if (isRcon(o)) return ID_RCON;
		if (isBlock(o)) return ID_BLOCK;
		return IDPREFIX+((CommandSender)o).getName();
	}
	
	// -------------------------------------------- //
	// GET SENDER
	// -------------------------------------------- //
	
	// ACTUALL LOGIC
	
	public static synchronized CommandSender getSender(String senderId)
	{
		if (senderId == null) return null;
		if (isPlayerId(senderId))
		{
			return Bukkit.getPlayerExact(senderId);
		}
		return idToSender.get(senderId);
	}
	
	// ID STUFF

	public static Player getPlayer(String senderId)
	{
		return getAsPlayer(getSender(senderId));
	}
	
	public static ConsoleCommandSender getConsole(String senderId)
	{
		return getAsConsole(getSender(senderId));
	}
	
	public static RemoteConsoleCommandSender getRcon(String senderId)
	{
		return getAsRcon(getSender(senderId));
	}
	
	public static BlockCommandSender getBlock(String senderId)
	{
		return getAsBlock(getSender(senderId));
	}
	
	// MARCHAL STUFF
	
	public static CommandSender getAsSender(Object o)
	{
		if (!isSender(o)) return null;
		return (CommandSender) o;
	}
	
	public static Player getAsPlayer(Object o)
	{
		if (!isPlayer(o)) return null;
		return (Player) o;
	}
	
	public static ConsoleCommandSender getAsConsole(Object o)
	{
		if (!isConsole(o)) return null;
		return (ConsoleCommandSender) o;
	}
	
	public static RemoteConsoleCommandSender getAsRcon(Object o)
	{
		if (!isRcon(o)) return null;
		return (RemoteConsoleCommandSender) o;
	}
	
	public static BlockCommandSender getAsBlock(Object o)
	{
		if (!isBlock(o)) return null;
		return (BlockCommandSender) o;
	}
	
	// -------------------------------------------- //
	// GET STANDARD-NON-PLAYERS
	// -------------------------------------------- //
	
	public static ConsoleCommandSender getConsole()
	{
		return Bukkit.getConsoleSender();
	}
	
	public static RemoteConsoleCommandSender getRcon()
	{
		Server server = Bukkit.getServer();
		CraftServer craftServer = (CraftServer)server;
		MinecraftServer minecraftServer = craftServer.getServer();
		return minecraftServer.remoteConsole;
	}
	
	public static BlockCommandSender getBlock()
	{
		return FakeBlockCommandSender.get();
	}
	
	// -------------------------------------------- //
	// GET ALL ONLINE
	// -------------------------------------------- //
	
	public static List<CommandSender> getOnlineSenders()
	{
		List<CommandSender> ret = new ArrayList<CommandSender>(Arrays.asList(Bukkit.getOnlinePlayers()));
		for (Entry<String, CommandSender> entry : idToSender.entrySet())
		{
			String id = entry.getKey();
			CommandSender sender = entry.getValue();
			if (isPlayerId(id)) continue;
			ret.add(sender);
		}
		return ret;
	}
	
	// -------------------------------------------- //
	// CONVENIENCE GAME-MODE
	// -------------------------------------------- //
	
	public static GameMode getGameMode(String senderId, GameMode def)
	{
		Player player = getPlayer(senderId);
		if (player == null) return def;
		return player.getGameMode();
	}

	public static boolean isGameMode(String senderId, GameMode gm, boolean def)
	{
		Player player = getPlayer(senderId);
		if (player == null) return def;
		return player.getGameMode() == gm;
	}
	
	public static GameMode getGameMode(CommandSender sender, GameMode def)
	{
		return getGameMode(getSenderId(sender), def);
	}

	public static boolean isGameMode(CommandSender sender, GameMode gm, boolean def)
	{
		return isGameMode(getSenderId(sender), gm, def);
	}
	
}
