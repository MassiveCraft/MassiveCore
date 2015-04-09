package com.massivecraft.massivecore.mixin;

import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.permissions.Permissible;

import com.massivecraft.massivecore.teleport.Destination;

public interface TeleportMixin
{
	// -------------------------------------------- //
	// CHECK
	// -------------------------------------------- //
	
	public boolean isCausedByMixin(PlayerTeleportEvent event);
	
	// PERMUTATION
	// # to
	// PS
	// CommandSender
	// SenderEntity
	// String
	// PSGetter
	
	// -------------------------------------------- //
	// COMMAND SENDER
	// -------------------------------------------- //
	
	public void teleport(Object teleporteeObject, Destination destination) throws TeleporterException;
	public void teleport(Object teleporteeObject, Destination destination, Permissible delayPermissible) throws TeleporterException;
	public void teleport(Object teleporteeObject, Destination destination, int delaySeconds) throws TeleporterException;
	
}
