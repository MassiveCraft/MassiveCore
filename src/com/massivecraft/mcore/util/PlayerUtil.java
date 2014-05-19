package com.massivecraft.mcore.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.massivecraft.mcore.MCore;

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
	
	private static Map<UUID, PlayerDeathEvent> idToDeath = new HashMap<UUID, PlayerDeathEvent>();
	
	private static Map<UUID, Long> idToLastMoveMillis = new HashMap<UUID, Long>(); 
	
	// -------------------------------------------- //
	// SETUP
	// -------------------------------------------- //
	
	public void setup()
	{
		idToDeath.clear();
		
		joinedPlayerNames.clear();
		for (Player player : Bukkit.getOnlinePlayers())
		{
			joinedPlayerNames.add(player.getName());
		}
		
		idToLastMoveMillis.clear();
		
		Bukkit.getPluginManager().registerEvents(this, MCore.get());
	}
	
	// -------------------------------------------- //
	// LAST MOVE & STAND STILL (MILLIS)
	// -------------------------------------------- //
	
	public static void setLastMoveMillis(Player player, long millis)
	{
		if (player == null) return;
		idToLastMoveMillis.put(player.getUniqueId(), millis);
	}
	
	public static void setLastMoveMillis(Player player)
	{
		setLastMoveMillis(player, System.currentTimeMillis());
	}
	
	public static long getLastMoveMillis(Player player)
	{
		if (player == null) return 0;
		Long ret = idToLastMoveMillis.get(player.getUniqueId());
		if (ret == null) return 0;
		return ret;
	}
	
	public static long getStandStillMillis(Player player)
	{
		if (player == null) return 0;
		if (player.isDead()) return 0;
		if (!player.isOnline()) return 0;
		
		Long ret = idToLastMoveMillis.get(player.getUniqueId());
		if (ret == null) return 0;
		
		ret = System.currentTimeMillis() - ret;
		
		return ret;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void setLastMoveMillis(PlayerMoveEvent event)
	{
		if (MUtil.isSameBlock(event)) return;
		setLastMoveMillis(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void setLastMoveMillis(PlayerJoinEvent event)
	{
		setLastMoveMillis(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void setLastMoveMillis(PlayerChangedWorldEvent event)
	{
		setLastMoveMillis(event.getPlayer());
	}
	
	// -------------------------------------------- //
	// IS DUPLICATE DEATH EVENT
	// -------------------------------------------- //
	// Some times when players die the PlayerDeathEvent is fired twice.
	// We want to ignore the extra calls.
	
	public static boolean isDuplicateDeathEvent(PlayerDeathEvent event)
	{
		// Prepare the lowercase name ...
		final UUID id = event.getEntity().getUniqueId();
		
		// ... take a look at the currently stored event ...
		PlayerDeathEvent current = idToDeath.get(id);
		
		if (current != null) return !current.equals(event);
		
		// ... otherwise store ... 
		idToDeath.put(id, event);
		
		// ... schedule removal ...
		Bukkit.getScheduler().scheduleSyncDelayedTask(MCore.get(), new Runnable()
		{
			@Override
			public void run()
			{
				idToDeath.remove(id);
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
		// No need for nms anymore.
		// We can trigger this packet through the use of this bukkit api method:
		player.setHealthScaled(player.isHealthScaled());
		/*
		CraftPlayer cplayer = (CraftPlayer)player;
		EntityPlayer eplayer = cplayer.getHandle();
		eplayer.playerConnection.sendPacket(new PacketPlayOutUpdateHealth(cplayer.getScaledHealth(), eplayer.getFoodData().a(), eplayer.getFoodData().e()));
		*/
	}
	
}
