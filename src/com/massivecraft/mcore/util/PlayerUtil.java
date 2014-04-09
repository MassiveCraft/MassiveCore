package com.massivecraft.mcore.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;

import net.minecraft.server.v1_7_R2.EntityPlayer;
import net.minecraft.server.v1_7_R2.PacketPlayOutUpdateHealth;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.MCoreMPlayer;

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
	
	public static String getPlayerName(final UUID playerId, final boolean usingCache, final boolean usingMojangApi)
	{
		List<UUID> playerIds = Collections.singletonList(playerId);
		Map<UUID, String> map = getPlayerNames(playerIds, usingCache, usingMojangApi);
		return map.get(playerId);
	}
	public static String getPlayerName(final UUID playerId, final boolean usingCache)
	{
		return getPlayerName(playerId, usingCache, true);
	}
	public static String getPlayerName(final UUID playerId)
	{
		return getPlayerName(playerId, true);
	}
	
	public static UUID getPlayerId(final String playerName, final boolean usingCache, final boolean usingMojangApi)
	{
		List<String> playerNames = Collections.singletonList(playerName);
		Map<String, UUID> map = getPlayerIds(playerNames, usingCache, usingMojangApi);
		return map.get(playerName);
	}
	public static UUID getPlayerId(final String playerName, final boolean usingCache)
	{
		return getPlayerId(playerName, usingCache, true);
	}
	public static UUID getPlayerId(final String playerName)
	{
		return getPlayerId(playerName, true);
	}
	
	// I suggest using ...
	// final Map<String, UUID> ret = new TreeMap<String, UUID>(String.CASE_INSENSITIVE_ORDER);
	// ... since you achieve case insensitivity that way.
	public static Map<String, UUID> getPlayerIds(Collection<String> playerNames, final boolean usingCache, final boolean usingMojangApi, final boolean synchronous, final Runnable onComplete, Map<String, UUID> ret)
	{
		// Finalize Args
		// To run the Async task we need final versions of all arguments. 
		// We shallow copy the array for the sake of concurrency and that we will want to remove those we could handle from cache in order to avoid contacting the mojang api in vain.
		// We need a return value map. Create one if null. Here we do however not shallow copy. We aim to edit the supplied map instance so that it can be used inside the onComplete Runnable.
		final List<String> playerNamesFinal = new ArrayList<String>(playerNames);
		final Map<String, UUID> retFinal = (ret != null ? ret : new TreeMap<String, UUID>(String.CASE_INSENSITIVE_ORDER));
		
		// Handle Async
		// Just run sync from another thread.
		if (!synchronous)
		{
			Bukkit.getScheduler().runTaskAsynchronously(MCore.get(), new Runnable()
			{
				@Override
				public void run()
				{
					PlayerUtil.getPlayerIds(playerNamesFinal, usingCache, usingMojangApi, true, onComplete, retFinal);
				}
			});
			return retFinal;
		}
		
		// Handle Cache
		if (usingCache)
		{
			Iterator<String> iter = playerNamesFinal.iterator();
			while (iter.hasNext())
			{
				String playerName = iter.next();
				MCoreMPlayer mplayer = MCoreMPlayer.get(playerName);
				if (mplayer == null) continue;
				retFinal.put(mplayer.getName(), UUID.fromString(mplayer.getId()));
				iter.remove();
			}	
		}
		
		// Handle Mojang Api
		if (usingMojangApi && playerNamesFinal.size() > 0)
		{
			try
			{
				Map<String, UUID> mojangApiResult = MojangApiUtil.getPlayerIds(playerNamesFinal);
				// Add to the cache
				for (Entry<String, UUID> entry : mojangApiResult.entrySet())
				{
					String name = entry.getKey();
					UUID id = entry.getValue();
					MCoreMPlayer mplayer = MCoreMPlayer.get(id, true);
					mplayer.setName(name);
				}
				// Add to the return value
				retFinal.putAll(mojangApiResult);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		// Run the onComplete task.
		if (onComplete != null)
		{
			onComplete.run();
		}
		
		// Return
		return retFinal;
	}
	public static Map<String, UUID> getPlayerIds(Collection<String> playerNames, final boolean usingCache, final boolean usingMojangApi, final boolean synchronous, final Runnable onComplete)
	{
		return getPlayerIds(playerNames, usingCache, usingMojangApi, synchronous, onComplete, null);
	}
	public static Map<String, UUID> getPlayerIds(Collection<String> playerNames, final boolean usingCache, final boolean usingMojangApi, final boolean synchronous)
	{
		return getPlayerIds(playerNames, usingCache, usingMojangApi, synchronous, null);
	}
	public static Map<String, UUID> getPlayerIds(Collection<String> playerNames, final boolean usingCache, final boolean usingMojangApi)
	{
		return getPlayerIds(playerNames, usingCache, usingMojangApi, true);
	}
	public static Map<String, UUID> getPlayerIds(Collection<String> playerNames, final boolean usingCache)
	{
		return getPlayerIds(playerNames, usingCache, true);
	}
	public static Map<String, UUID> getPlayerIds(Collection<String> playerNames)
	{
		return getPlayerIds(playerNames, true);
	}
	
	public static Map<UUID, String> getPlayerNames(Collection<UUID> playerIds, final boolean usingCache, final boolean usingMojangApi, final boolean synchronous, final Runnable onComplete, Map<UUID, String> ret)
	{
		// Finalize Args
		// To run the Async task we need final versions of all arguments. 
		// We shallow copy the array for the sake of concurrency and that we will want to remove those we could handle from cache in order to avoid contacting the mojang api in vain.
		// We need a return value map. Create one if null. Here we do however not shallow copy. We aim to edit the supplied map instance so that it can be used inside the onComplete Runnable.
		final List<UUID> playerIdsFinal = new ArrayList<UUID>(playerIds);
		final Map<UUID, String> retFinal = (ret != null ? ret : new HashMap<UUID, String>());
		
		// Handle Async
		// Just run sync from another thread.
		if (!synchronous)
		{
			Bukkit.getScheduler().runTaskAsynchronously(MCore.get(), new Runnable()
			{
				@Override
				public void run()
				{
					PlayerUtil.getPlayerNames(playerIdsFinal, usingCache, usingMojangApi, true, onComplete, retFinal);
				}
			});
			return retFinal;
		}
		
		// Handle Cache
		if (usingCache)
		{
			Iterator<UUID> iter = playerIdsFinal.iterator();
			while (iter.hasNext())
			{
				UUID playerId = iter.next();
				MCoreMPlayer mplayer = MCoreMPlayer.get(playerId);
				if (mplayer == null) continue;
				retFinal.put(playerId, mplayer.getName());
				iter.remove();
			}	
		}
		
		// Handle Mojang Api
		if (usingMojangApi && playerIdsFinal.size() > 0)
		{
			try
			{
				Map<UUID, String> mojangApiResult = MojangApiUtil.getPlayerNames(playerIdsFinal);
				// Add to the cache
				for (Entry<UUID, String> entry : mojangApiResult.entrySet())
				{
					UUID id = entry.getKey();
					String name = entry.getValue();
					MCoreMPlayer mplayer = MCoreMPlayer.get(id, true);
					mplayer.setName(name);
				}
				// Add to the return value
				retFinal.putAll(mojangApiResult);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		// Run the onComplete task.
		if (onComplete != null)
		{
			onComplete.run();
		}
		
		// Return
		return retFinal;
	}
	public static Map<UUID, String> getPlayerNames(Collection<UUID> playerIds, final boolean usingCache, final boolean usingMojangApi, final boolean synchronous, final Runnable onComplete)
	{
		return getPlayerNames(playerIds, usingCache, usingMojangApi, synchronous, onComplete, null);
	}
	public static Map<UUID, String> getPlayerNames(Collection<UUID> playerIds, final boolean usingCache, final boolean usingMojangApi, final boolean synchronous)
	{
		return getPlayerNames(playerIds, usingCache, usingMojangApi, synchronous, null);
	}
	public static Map<UUID, String> getPlayerNames(Collection<UUID> playerIds, final boolean usingCache, final boolean usingMojangApi)
	{
		return getPlayerNames(playerIds, usingCache, usingMojangApi, true);
	}
	public static Map<UUID, String> getPlayerNames(Collection<UUID> playerIds, final boolean usingCache)
	{
		return getPlayerNames(playerIds, usingCache, true);
	}
	public static Map<UUID, String> getPlayerNames(Collection<UUID> playerIds)
	{
		return getPlayerNames(playerIds, true);
	}
	
}
