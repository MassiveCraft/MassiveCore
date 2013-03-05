package com.massivecraft.mcore.util;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.massivecraft.mcore.MCore;

/**
 * The first teleport to ever occur for a player happens soon after the player logged in.
 * This is a system one that should not be cancelled since it happens as the player is assigned their entrance position.
 * Altering the to-location is possible but altering the world will not matter. Only x, y, z, pitch and yaw are taken into consideration.
 * This tool can be used to check if a PlayerTeleportEvent is the first one for the player.
 */
public class FirstTeleportUtil implements Listener
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static FirstTeleportUtil i = new FirstTeleportUtil();
	public static FirstTeleportUtil get() { return i; }
	
	// -------------------------------------------- //
	// STATIC
	// -------------------------------------------- //
	
	private Map<Player, PlayerTeleportEvent> playerToFirstTeleport;
	
	public static boolean isFirstTeleport(PlayerTeleportEvent event)
	{
		Player player = event.getPlayer();
		
		PlayerTeleportEvent stored = i.playerToFirstTeleport.get(player);
		if (stored == null)
		{
			i.playerToFirstTeleport.put(player, event);
			return true;
		}
		else
		{
			return stored == event;
		}
	}
	
	// -------------------------------------------- //
	// SETUP
	// -------------------------------------------- //
	
	public void setup()
	{
		playerToFirstTeleport = new HashMap<Player, PlayerTeleportEvent>();
		Bukkit.getPluginManager().registerEvents(this, MCore.get());
	}
	
	// -------------------------------------------- //
	// LISTENER
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerLoginEventMonitor(PlayerLoginEvent event)
	{
		Player player = event.getPlayer();
		this.playerToFirstTeleport.remove(player);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerTeleportLowest(PlayerTeleportEvent event)
	{
		Player player = event.getPlayer();
		if (this.playerToFirstTeleport.containsKey(player)) return;
		this.playerToFirstTeleport.put(player, event);
	}
	
}
