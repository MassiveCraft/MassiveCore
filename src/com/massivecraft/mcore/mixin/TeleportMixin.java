package com.massivecraft.mcore.mixin;

import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.permissions.Permissible;

import com.massivecraft.mcore.ps.PS;
import com.massivecraft.mcore.store.SenderEntity;
import com.massivecraft.mcore.teleport.PSGetter;

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
	
	// PS
	public void teleport(Object teleporteeObject, PS to) throws TeleporterException;
	public void teleport(Object teleporteeObject, PS to, String desc) throws TeleporterException;
	public void teleport(Object teleporteeObject, PS to, String desc, Permissible delayPermissible) throws TeleporterException;
	public void teleport(Object teleporteeObject, PS to, String desc, int delaySeconds) throws TeleporterException;
	
	// CommandSender
	public void teleport(Object teleporteeObject, CommandSender to) throws TeleporterException;
	public void teleport(Object teleporteeObject, CommandSender to, String desc) throws TeleporterException;
	public void teleport(Object teleporteeObject, CommandSender to, String desc, Permissible delayPermissible) throws TeleporterException;
	public void teleport(Object teleporteeObject, CommandSender to, String desc, int delaySeconds) throws TeleporterException;
	
	// SenderEntity
	public void teleport(Object teleporteeObject, SenderEntity<?> to) throws TeleporterException;
	public void teleport(Object teleporteeObject, SenderEntity<?> to, String desc) throws TeleporterException;
	public void teleport(Object teleporteeObject, SenderEntity<?> to, String desc, Permissible delayPermissible) throws TeleporterException;
	public void teleport(Object teleporteeObject, SenderEntity<?> to, String desc, int delaySeconds) throws TeleporterException;
	
	// String
	public void teleport(Object teleporteeObject, String to) throws TeleporterException;
	public void teleport(Object teleporteeObject, String to, String desc) throws TeleporterException;
	public void teleport(Object teleporteeObject, String to, String desc, Permissible delayPermissible) throws TeleporterException;
	public void teleport(Object teleporteeObject, String to, String desc, int delaySeconds) throws TeleporterException;
	
	// PSGetter
	public void teleport(Object teleporteeObject, PSGetter to) throws TeleporterException;
	public void teleport(Object teleporteeObject, PSGetter to, String desc) throws TeleporterException;
	public void teleport(Object teleporteeObject, PSGetter to, String desc, Permissible delayPermissible) throws TeleporterException;
	public void teleport(Object teleporteeObject, PSGetter to, String desc, int delaySeconds) throws TeleporterException;
	
}
