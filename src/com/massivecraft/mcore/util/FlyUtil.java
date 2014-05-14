package com.massivecraft.mcore.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.massivecraft.mcore.event.MCorePlayerFlyEvent;

public class FlyUtil
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public final static float DEFAULT_SPEED = 0.1f;

	// -------------------------------------------- //
	// LAST ALLOWED
	// -------------------------------------------- //
	
	public static Map<UUID, Long> idToLastActive = new HashMap<UUID, Long>();
	
	public static Long getLastActive(Player player)
	{
		return idToLastActive.get(player.getUniqueId());
	}
	
	public static void setLastActive(Player player, Long millis)
	{
		idToLastActive.put(player.getUniqueId(), millis);
	}
	
	// -------------------------------------------- //
	// UPDATE BY EVENT
	// -------------------------------------------- //
	
	public static void update(Player player)
	{
		MCorePlayerFlyEvent event = new MCorePlayerFlyEvent(player);
		event.run();
		
		setAllowed(player, event.isAllowed());
		setActive(player, event.isActive());
		setSpeed(player, event.getSpeed());
	}
	
	public static void reset(Player player)
	{
		setAllowed(player, player.getGameMode() == GameMode.CREATIVE);
		setActive(player, player.getGameMode() == GameMode.CREATIVE);
		setSpeed(player, DEFAULT_SPEED);
		update(player);
	}
	
	// -------------------------------------------- //
	// ALLOWED
	// -------------------------------------------- //
	
	public static boolean setAllowed(Player player, boolean allowed)
	{	
		// NoChange
		if (isAllowed(player) == allowed) return false;
		
		// Apply
		player.setFallDistance(0);
		player.setAllowFlight(allowed);
		player.setFallDistance(0);
		
		// Return
		return true;
	}
	
	public static boolean isAllowed(Player player)
	{
		return player.getAllowFlight();
	}
	
	// -------------------------------------------- //
	// ACTIVE
	// -------------------------------------------- //
	
	public static boolean setActive(Player player, boolean active)
	{
		// Last Active Update
		if (active)
		{
			setLastActive(player, System.currentTimeMillis());
		}
		
		// NoChange
		if (isActive(player) == active) return false;
		
		// Apply
		player.setFallDistance(0);
		player.setFlying(active);
		player.setFallDistance(0);
		
		// Return
		return true;
	}
	
	public static boolean isActive(Player player)
	{
		return player.isFlying();
	}
	
	// -------------------------------------------- //
	// ACTIVE
	// -------------------------------------------- //
	
	public static boolean setSpeed(Player player, float speed)
	{
		// NoChange
		if (getSpeed(player) == speed) return false;
		
		// Apply
		player.setFallDistance(0);
		player.setFlySpeed(speed);
		player.setFallDistance(0);
		
		// Return
		return true;
	}
	
	public static float getSpeed(Player player)
	{
		return player.getFlySpeed();
	}
	
}
