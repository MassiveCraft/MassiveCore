package com.massivecraft.mcore.mixin;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

import com.massivecraft.mcore.ps.PS;

public interface TeleportMixin
{
	// -------------------------------------------- //
	// PLAYER
	// -------------------------------------------- //
	
	public void teleport(Player teleportee, PS destinationPs) throws TeleporterException;
	
	public void teleport(Player teleportee, PS destinationPs, String destinationDesc) throws TeleporterException;
	
	public void teleport(Player teleportee, PS destinationPs, String destinationDesc, Permissible delayPermissible) throws TeleporterException;
	
	public void teleport(Player teleportee, PS destinationPs, String destinationDesc, int delaySeconds) throws TeleporterException;
	
	// -------------------------------------------- //
	// PLAYER ID
	// -------------------------------------------- //
	
	public void teleport(String teleporteeId, PS destinationPs) throws TeleporterException;
	
	public void teleport(String teleporteeId, PS destinationPs, String destinationDesc) throws TeleporterException;
	
	public void teleport(String teleporteeId, PS destinationPs, String destinationDesc, Permissible delayPermissible) throws TeleporterException;
	
	// The only one not covered in abstract
	public void teleport(String teleporteeId, PS destinationPs, String destinationDesc, int delaySeconds) throws TeleporterException;
}
