package com.massivecraft.mcore.mixin;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.permissions.Permissible;

import com.massivecraft.mcore.ps.PS;

public interface TeleportMixin
{
	// -------------------------------------------- //
	// CHECK
	// -------------------------------------------- //
	
	public boolean isCausedByMixin(PlayerTeleportEvent event);
	
	// -------------------------------------------- //
	// PLAYER
	// -------------------------------------------- //
	
	public void teleport(Player teleportee, PS to) throws TeleporterException;
	
	public void teleport(Player teleportee, PS to, String desc) throws TeleporterException;
	
	public void teleport(Player teleportee, PS to, String desc, Permissible delayPermissible) throws TeleporterException;
	
	public void teleport(Player teleportee, PS to, String desc, int delaySeconds) throws TeleporterException;
	
	// -------------------------------------------- //
	// PLAYER ID
	// -------------------------------------------- //
	
	public void teleport(String teleporteeId, PS to) throws TeleporterException;
	
	public void teleport(String teleporteeId, PS to, String desc) throws TeleporterException;
	
	public void teleport(String teleporteeId, PS to, String desc, Permissible delayPermissible) throws TeleporterException;
	
	// The only one not covered in abstract
	public void teleport(String teleporteeId, PS to, String desc, int delaySeconds) throws TeleporterException;
}
