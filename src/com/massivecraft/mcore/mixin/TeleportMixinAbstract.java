package com.massivecraft.mcore.mixin;

import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.permissions.Permissible;

import com.massivecraft.mcore.MCoreConf;
import com.massivecraft.mcore.ps.PS;
import com.massivecraft.mcore.store.SenderEntity;
import com.massivecraft.mcore.teleport.PSGetter;
import com.massivecraft.mcore.teleport.PSGetterPS;
import com.massivecraft.mcore.teleport.PSGetterPlayer;

public abstract class TeleportMixinAbstract implements TeleportMixin
{
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static int getTpdelay(Permissible delayPermissible)
	{
		return MCoreConf.get().getTpdelay(delayPermissible);
	}
	
	// -------------------------------------------- //
	// CHECK
	// -------------------------------------------- //
	
	@Override
	public boolean isCausedByMixin(PlayerTeleportEvent event)
	{
		return EngineTeleportMixinCause.get().isCausedByTeleportMixin(event);
	}
	
	// -------------------------------------------- //
	// SENDER OBJECT
	// -------------------------------------------- //
	
	// PS
	@Override
	public void teleport(Object teleportee, PS to) throws TeleporterException
	{
		this.teleport(teleportee, to, null);
	}
	
	@Override
	public void teleport(Object teleportee, PS to, String desc) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, 0);
	}
	
	@Override
	public void teleport(Object teleportee, PS to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, getTpdelay(delayPermissible));
	}
	
	@Override
	public void teleport(Object teleportee, PS to, String desc, int delaySeconds) throws TeleporterException
	{
		this.teleport(teleportee, PSGetterPS.valueOf(to), desc, delaySeconds);
	}
	
	// CommandSender
	@Override
	public void teleport(Object teleportee, CommandSender to) throws TeleporterException
	{
		this.teleport(teleportee, to, null);
	}
	
	@Override
	public void teleport(Object teleportee, CommandSender to, String desc) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, 0);
	}
	
	@Override
	public void teleport(Object teleportee, CommandSender to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, getTpdelay(delayPermissible));
	}
	
	@Override
	public void teleport(Object teleportee, CommandSender to, String desc, int delaySeconds) throws TeleporterException
	{
		this.teleport(teleportee, PSGetterPlayer.valueOf(to), desc, delaySeconds);
	}
	
	// SenderEntity
	@Override
	public void teleport(Object teleportee, SenderEntity<?> to) throws TeleporterException
	{
		this.teleport(teleportee, to, null);
	}
	
	@Override
	public void teleport(Object teleportee, SenderEntity<?> to, String desc) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, 0);
	}
	
	@Override
	public void teleport(Object teleportee, SenderEntity<?> to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, getTpdelay(delayPermissible));
	}
	
	@Override
	public void teleport(Object teleportee, SenderEntity<?> to, String desc, int delaySeconds) throws TeleporterException
	{
		this.teleport(teleportee, PSGetterPlayer.valueOf(to), desc, delaySeconds);
	}
	
	// String
	@Override
	public void teleport(Object teleportee, String to) throws TeleporterException
	{
		this.teleport(teleportee, to, null);
	}
	
	@Override
	public void teleport(Object teleportee, String to, String desc) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, 0);
	}
	
	@Override
	public void teleport(Object teleportee, String to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, getTpdelay(delayPermissible));
	}
	
	@Override
	public void teleport(Object teleportee, String to, String desc, int delaySeconds) throws TeleporterException
	{
		this.teleport(teleportee, PSGetterPlayer.valueOf(to), desc, delaySeconds);
	}
	
	// PSGetter
	@Override
	public void teleport(Object teleportee, PSGetter to) throws TeleporterException
	{
		this.teleport(teleportee, to, null);
	}
	
	@Override
	public void teleport(Object teleportee, PSGetter to, String desc) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, 0);
	}
	
	@Override
	public void teleport(Object teleportee, PSGetter to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, getTpdelay(delayPermissible));
	}
	
	// To implement!
	/*@Override
	public void teleport(Object teleportee, PSGetter to, String desc, int delaySeconds) throws TeleporterException
	{
		//this.teleport(teleportee, to, desc, delaySeconds);
	}*/
}
