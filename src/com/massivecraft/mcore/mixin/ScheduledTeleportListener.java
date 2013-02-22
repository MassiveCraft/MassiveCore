package com.massivecraft.mcore.mixin;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.util.MUtil;

public class ScheduledTeleportListener implements Listener
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ScheduledTeleportListener i = new ScheduledTeleportListener();
	public static ScheduledTeleportListener get() { return i; }
	
	// -------------------------------------------- //
	// SETUP
	// -------------------------------------------- //
	
	public void setup()
	{
		Bukkit.getPluginManager().registerEvents(this, MCore.get());
	}
	
	// -------------------------------------------- //
	// LISTENER
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerMoved(PlayerMoveEvent event)
	{
		// If the player moved from one block to another ...
		if (MUtil.isSameBlock(event.getFrom(), event.getTo())) return;
		
		// ... and there is a ScheduledTeleport ...
		ScheduledTeleport scheduledTeleport = ScheduledTeleport.teleporteeToScheduledTeleport.get(event.getPlayer());
		if (scheduledTeleport == null) return;
		
		// ... unschedule it ...
		scheduledTeleport.unschedule();
		
		// ... and inform the teleportee.
		Mixin.msg(scheduledTeleport.getTeleportee(), "<rose>Cancelled <i>teleport to <h>"+scheduledTeleport.getDestinationDesc()+"<i>.");
	}
	
}
