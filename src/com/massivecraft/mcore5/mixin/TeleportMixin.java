package com.massivecraft.mcore5.mixin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

import com.massivecraft.mcore5.PS;

public interface TeleportMixin
{
	// -------------------------------------------- //
	// MESSAGING
	// -------------------------------------------- //
	
	public void sendPreTeleportMessage(Player teleportee, String destinationDesc, int delaySeconds);
	
	// -------------------------------------------- //
	// PLAYER
	// -------------------------------------------- //
	
	public void teleport(Player teleportee, PS destinationPs) throws TeleporterException;
	
	public void teleport(Player teleportee, PS destinationPs, String destinationDesc) throws TeleporterException;
	
	public void teleport(Player teleportee, PS destinationPs, String destinationDesc, Permissible delayPermissible) throws TeleporterException;
	public void teleport(Player teleportee, PS destinationPs, String destinationDesc, Permissible delayPermissible, CommandSender otherSender, String otherPerm) throws TeleporterException;
	
	public void teleport(Player teleportee, PS destinationPs, String destinationDesc, CommandSender otherSender, String otherPerm) throws TeleporterException;
	
	public void teleport(Player teleportee, PS destinationPs, String destinationDesc, int delaySeconds, CommandSender otherSender, String otherPerm) throws TeleporterException;
	
	public void teleport(Player teleportee, PS destinationPs, String destinationDesc, int delaySeconds) throws TeleporterException;
	
	// -------------------------------------------- //
	// PLAYER ID
	// -------------------------------------------- //
	
	public void teleport(String teleporteeId, PS destinationPs) throws TeleporterException;
	
	public void teleport(String teleporteeId, PS destinationPs, String destinationDesc) throws TeleporterException;
	
	public void teleport(String teleporteeId, PS destinationPs, String destinationDesc, Permissible delayPermissible) throws TeleporterException;
	public void teleport(String teleporteeId, PS destinationPs, String destinationDesc, Permissible delayPermissible, CommandSender otherSender, String otherPerm) throws TeleporterException;
	
	public void teleport(String teleporteeId, PS destinationPs, String destinationDesc, CommandSender otherSender, String otherPerm) throws TeleporterException;
	
	public void teleport(String teleporteeId, PS destinationPs, String destinationDesc, int delaySeconds, CommandSender otherSender, String otherPerm) throws TeleporterException;
	
	public void teleport(String teleporteeId, PS destinationPs, String destinationDesc, int delaySeconds) throws TeleporterException;
}
