package com.massivecraft.mcore.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;

import net.minecraft.server.v1_7_R3.EntityPlayer;
import net.minecraft.server.v1_7_R3.PacketPlayOutUpdateHealth;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.MCoreMPlayer;
import com.massivecraft.mcore.fetcher.FetcherPlayerIdCached;
import com.massivecraft.mcore.fetcher.FetcherPlayerNameCached;
import com.massivecraft.mcore.mixin.Mixin;
import com.massivecraft.mcore.store.Coll;
import com.massivecraft.mcore.store.SenderColl;

public class PlayerUtil implements Listener
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static PlayerUtil i = new PlayerUtil();
	public static PlayerUtil get() { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private static Set<String> joinedPlayerNames = new ConcurrentSkipListSet<String>(String.CASE_INSENSITIVE_ORDER);
	
	private static Map<String, PlayerDeathEvent> lowercaseToDeath = new HashMap<String, PlayerDeathEvent>();
	
	// -------------------------------------------- //
	// SETUP
	// -------------------------------------------- //
	
	public void setup()
	{
		lowercaseToDeath.clear();
		
		joinedPlayerNames.clear();
		for (Player player : Bukkit.getOnlinePlayers())
		{
			joinedPlayerNames.add(player.getName());
		}
		
		Bukkit.getPluginManager().registerEvents(this, MCore.get());
	}
	
	// -------------------------------------------- //
	// IS DUPLICATE DEATH EVENT
	// -------------------------------------------- //
	// Some times when players die the PlayerDeathEvent is fired twice.
	// We want to ignore the extra calls.
	
	public static boolean isDuplicateDeathEvent(PlayerDeathEvent event)
	{
		// Prepare the lowercase name ...
		final String lowercase = event.getEntity().getName().toLowerCase();
		
		// ... take a look at the currently stored event ...
		PlayerDeathEvent current = lowercaseToDeath.get(lowercase);
		
		if (current != null) return !current.equals(event);
		
		// ... otherwise store ... 
		lowercaseToDeath.put(lowercase, event);
		
		// ... schedule removal ...
		Bukkit.getScheduler().scheduleSyncDelayedTask(MCore.get(), new Runnable()
		{
			@Override
			public void run()
			{
				lowercaseToDeath.remove(lowercase);
			}
		});
		
		// ... and return the fact that it was not a duplicate.
		return false;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void isDuplicateDeathEventLowest(PlayerDeathEvent event)
	{
		isDuplicateDeathEvent(event);
	}
	
	// -------------------------------------------- //
	// IS JOINED
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void joinMonitor(PlayerJoinEvent event)
	{
		final String playerName = event.getPlayer().getName();
		Bukkit.getScheduler().scheduleSyncDelayedTask(MCore.get(), new Runnable()
		{
			@Override
			public void run()
			{
				joinedPlayerNames.add(playerName);
			}
		});
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void quitMonitor(PlayerQuitEvent event)
	{
		final String playerName = event.getPlayer().getName();
		joinedPlayerNames.remove(playerName);
	}
	
	public static boolean isJoined(Player player)
	{
		if (player == null) throw new NullPointerException("player was null");
		return joinedPlayerNames.contains(player.getName());
	}
	
	// -------------------------------------------- //
	// PACKET
	// -------------------------------------------- //
	
	/**
	 * Updates the players food and health information.
	 */
	public static void sendHealthFoodUpdatePacket(Player player)
	{
		CraftPlayer cplayer = (CraftPlayer)player;
		EntityPlayer eplayer = cplayer.getHandle();
		eplayer.playerConnection.sendPacket(new PacketPlayOutUpdateHealth(cplayer.getScaledHealth(), eplayer.getFoodData().a(), eplayer.getFoodData().e()));
	}
	
	// -------------------------------------------- //
	// PLAYER ID <---> PLAYER NAME
	// -------------------------------------------- //
	
	// Update Cache on Login
	@EventHandler(priority = EventPriority.LOWEST)
	public void playerIdPlayerName(PlayerLoginEvent event)
	{
		final String playerName = event.getPlayer().getName();
		final UUID playerId = event.getPlayer().getUniqueId();
		MCoreMPlayer mplayer = MCoreMPlayer.get(playerId, true);
		mplayer.setName(playerName);
	}
	
	public static Map<String, UUID> getPlayerIds(Collection<String> playerNames)
	{
		try
		{
			return FetcherPlayerIdCached.fetch(playerNames);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new TreeMap<String, UUID>(String.CASE_INSENSITIVE_ORDER);
		}
	}
	public static UUID getPlayerId(String playerName)
	{
		List<String> playerNames = Collections.singletonList(playerName);
		Map<String, UUID> map = getPlayerIds(playerNames);
		return map.get(playerName);
	}
	
	public static Map<UUID, String> getPlayerNames(Collection<UUID> playerIds)
	{
		try
		{
			return FetcherPlayerNameCached.fetch(playerIds);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new HashMap<UUID, String>();
		}
	}
	public static String getPlayerName(UUID playerId)
	{
		List<UUID> playerIds = Collections.singletonList(playerId);
		Map<UUID, String> map = getPlayerNames(playerIds);
		return map.get(playerId);
	}
	
	// -------------------------------------------- //
	// PLAYER ID <---> PLAYER NAME: FETCH ALL
	// -------------------------------------------- //
	
	public static void fetchAllIds()
	{
		// --- Starting Information ---
		MCore.get().log(Txt.parse("<a>============================================"));
		MCore.get().log(Txt.parse("<i>We are preparing for Mojangs switch to UUIDs."));
		MCore.get().log(Txt.parse("<i>Learn more at: <aqua>https://forums.bukkit.org/threads/psa-the-switch-to-uuids-potential-plugin-server-breakage.250915/"));
		MCore.get().log(Txt.parse("<i>Now fetching and caching UUID for all player names on this server!"));
		MCore.get().log(Txt.parse("<i>The mstore collection \"<h>mcore_mplayer<i>\" will contain the cached information."));
		
		// --- Find Player Names ---
		// Here we build a set containing all player names we know of!
		Set<String> playerNames = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		
		// All from mixin
		playerNames.addAll(Mixin.getAllPlayerIds());
		
		// All from sender colls
		for (Coll<?> coll : Coll.getInstances())
		{
			if (!(coll instanceof SenderColl<?>)) continue;
			playerNames.addAll(coll.getIds());
		}
		
		// Only valid player names
		Iterator<String> iter = playerNames.iterator();
		while (iter.hasNext())
		{
			String playerName =iter.next();
			if (MUtil.isValidPlayerName(playerName)) continue;
			iter.remove();
		}
		
		// Report: Player Names Found
		MCore.get().log(Txt.parse("<k>Player Names Found: <v>%d", playerNames.size()));
		
		// --- Remove Cached ---
		// Here we remove what we already have cached.
		iter = playerNames.iterator();
		int cached = 0;
		while (iter.hasNext())
		{
			String playerName = iter.next();
			MCoreMPlayer mplayer = MCoreMPlayer.get(playerName);
			if (mplayer == null) continue;
			if (mplayer.getName() == null) continue;
			cached++;
			iter.remove();
		}
		MCore.get().log(Txt.parse("<k>Player Names Cached: <v>%d", cached));
		MCore.get().log(Txt.parse("<k>Player Names Remaining: <v>%d", playerNames.size()));
		
		// --- Fetch ---
		// Here we fetch the remaining player names.
		// We fetch them through the cached fetcher.
		// This way we will use the mojang fetcher but also cache the result for the future.
		
		MCore.get().log(Txt.parse("<i>Now fetching the remaining players from Mojang API ..."));
		
		getPlayerIds(playerNames);
		
		MCore.get().log(Txt.parse("<g> ... done!"));
		MCore.get().log(Txt.parse("<i>(database saving will now commence which might lock the server for a while)"));
		MCore.get().log(Txt.parse("<a>============================================"));
	}
	
	public static <T> List<T> take(Collection<T> coll, int count)
	{
		List<T> ret = new ArrayList<T>();
		
		Iterator<T> iter = coll.iterator();
		int i = 0;
		while (iter.hasNext() && i < count)
		{
			i++;
			ret.add(iter.next());
			iter.remove();
		}
		
		return ret;
	}
	
}
