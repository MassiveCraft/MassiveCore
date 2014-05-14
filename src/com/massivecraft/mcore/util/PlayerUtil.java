package com.massivecraft.mcore.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
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
