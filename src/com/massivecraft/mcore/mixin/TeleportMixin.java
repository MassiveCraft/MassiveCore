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
	// 
	// # teleportee
	// CommandSender
	// SenderEntity
	// String
	// 
	// # to
	// PS
	// CommandSender
	// SenderEntity
	// String
	// PSGetter
	
	// -------------------------------------------- //
	// COMMAND SENDER
	// -------------------------------------------- //
	
	// CommandSender & PS
	public void teleport(CommandSender teleportee, PS to) throws TeleporterException;
	public void teleport(CommandSender teleportee, PS to, String desc) throws TeleporterException;
	public void teleport(CommandSender teleportee, PS to, String desc, Permissible delayPermissible) throws TeleporterException;
	public void teleport(CommandSender teleportee, PS to, String desc, int delaySeconds) throws TeleporterException;
	
	// CommandSender & CommandSender
	public void teleport(CommandSender teleportee, CommandSender to) throws TeleporterException;
	public void teleport(CommandSender teleportee, CommandSender to, String desc) throws TeleporterException;
	public void teleport(CommandSender teleportee, CommandSender to, String desc, Permissible delayPermissible) throws TeleporterException;
	public void teleport(CommandSender teleportee, CommandSender to, String desc, int delaySeconds) throws TeleporterException;
	
	// CommandSender & SenderEntity
	public void teleport(CommandSender teleportee, SenderEntity<?> to) throws TeleporterException;
	public void teleport(CommandSender teleportee, SenderEntity<?> to, String desc) throws TeleporterException;
	public void teleport(CommandSender teleportee, SenderEntity<?> to, String desc, Permissible delayPermissible) throws TeleporterException;
	public void teleport(CommandSender teleportee, SenderEntity<?> to, String desc, int delaySeconds) throws TeleporterException;
	
	// CommandSender & String
	public void teleport(CommandSender teleportee, String to) throws TeleporterException;
	public void teleport(CommandSender teleportee, String to, String desc) throws TeleporterException;
	public void teleport(CommandSender teleportee, String to, String desc, Permissible delayPermissible) throws TeleporterException;
	public void teleport(CommandSender teleportee, String to, String desc, int delaySeconds) throws TeleporterException;
	
	// CommandSender & PSGetter
	public void teleport(CommandSender teleportee, PSGetter to) throws TeleporterException;
	public void teleport(CommandSender teleportee, PSGetter to, String desc) throws TeleporterException;
	public void teleport(CommandSender teleportee, PSGetter to, String desc, Permissible delayPermissible) throws TeleporterException;
	public void teleport(CommandSender teleportee, PSGetter to, String desc, int delaySeconds) throws TeleporterException;
	
	//// SenderEntity
	
	// -------------------------------------------- //
	// SENDER ENTITY
	// -------------------------------------------- //
	
	// SenderEntity & PS
	public void teleport(SenderEntity<?> teleportee, PS to) throws TeleporterException;
	public void teleport(SenderEntity<?> teleportee, PS to, String desc) throws TeleporterException;
	public void teleport(SenderEntity<?> teleportee, PS to, String desc, Permissible delayPermissible) throws TeleporterException;
	public void teleport(SenderEntity<?> teleportee, PS to, String desc, int delaySeconds) throws TeleporterException;
	
	// SenderEntity & CommandSender
	public void teleport(SenderEntity<?> teleportee, CommandSender to) throws TeleporterException;
	public void teleport(SenderEntity<?> teleportee, CommandSender to, String desc) throws TeleporterException;
	public void teleport(SenderEntity<?> teleportee, CommandSender to, String desc, Permissible delayPermissible) throws TeleporterException;
	public void teleport(SenderEntity<?> teleportee, CommandSender to, String desc, int delaySeconds) throws TeleporterException;
	
	// SenderEntity & SenderEntity
	public void teleport(SenderEntity<?> teleportee, SenderEntity<?> to) throws TeleporterException;
	public void teleport(SenderEntity<?> teleportee, SenderEntity<?> to, String desc) throws TeleporterException;
	public void teleport(SenderEntity<?> teleportee, SenderEntity<?> to, String desc, Permissible delayPermissible) throws TeleporterException;
	public void teleport(SenderEntity<?> teleportee, SenderEntity<?> to, String desc, int delaySeconds) throws TeleporterException;
	
	// SenderEntity & String
	public void teleport(SenderEntity<?> teleportee, String to) throws TeleporterException;
	public void teleport(SenderEntity<?> teleportee, String to, String desc) throws TeleporterException;
	public void teleport(SenderEntity<?> teleportee, String to, String desc, Permissible delayPermissible) throws TeleporterException;
	public void teleport(SenderEntity<?> teleportee, String to, String desc, int delaySeconds) throws TeleporterException;
	
	// SenderEntity & PSGetter
	public void teleport(SenderEntity<?> teleportee, PSGetter to) throws TeleporterException;
	public void teleport(SenderEntity<?> teleportee, PSGetter to, String desc) throws TeleporterException;
	public void teleport(SenderEntity<?> teleportee, PSGetter to, String desc, Permissible delayPermissible) throws TeleporterException;
	public void teleport(SenderEntity<?> teleportee, PSGetter to, String desc, int delaySeconds) throws TeleporterException;
	
	//// String
	
	// -------------------------------------------- //
	// STRING
	// -------------------------------------------- //
	
	// String & PS
	public void teleport(String teleportee, PS to) throws TeleporterException;
	public void teleport(String teleportee, PS to, String desc) throws TeleporterException;
	public void teleport(String teleportee, PS to, String desc, Permissible delayPermissible) throws TeleporterException;
	public void teleport(String teleportee, PS to, String desc, int delaySeconds) throws TeleporterException;
	
	// String & CommandSender
	public void teleport(String teleportee, CommandSender to) throws TeleporterException;
	public void teleport(String teleportee, CommandSender to, String desc) throws TeleporterException;
	public void teleport(String teleportee, CommandSender to, String desc, Permissible delayPermissible) throws TeleporterException;
	public void teleport(String teleportee, CommandSender to, String desc, int delaySeconds) throws TeleporterException;
	
	// String & SenderEntity
	public void teleport(String teleportee, SenderEntity<?> to) throws TeleporterException;
	public void teleport(String teleportee, SenderEntity<?> to, String desc) throws TeleporterException;
	public void teleport(String teleportee, SenderEntity<?> to, String desc, Permissible delayPermissible) throws TeleporterException;
	public void teleport(String teleportee, SenderEntity<?> to, String desc, int delaySeconds) throws TeleporterException;
	
	// String & String
	public void teleport(String teleportee, String to) throws TeleporterException;
	public void teleport(String teleportee, String to, String desc) throws TeleporterException;
	public void teleport(String teleportee, String to, String desc, Permissible delayPermissible) throws TeleporterException;
	public void teleport(String teleportee, String to, String desc, int delaySeconds) throws TeleporterException;
	
	// String & PSGetter
	public void teleport(String teleportee, PSGetter to) throws TeleporterException;
	public void teleport(String teleportee, PSGetter to, String desc) throws TeleporterException;
	public void teleport(String teleportee, PSGetter to, String desc, Permissible delayPermissible) throws TeleporterException;
	public void teleport(String teleportee, PSGetter to, String desc, int delaySeconds) throws TeleporterException;
	// This very last method is is the core logic.
	// Everything else is implemented in the abstract.
	
}
