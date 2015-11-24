package com.massivecraft.massivecore.mixin;

import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.permissions.Permissible;

import com.massivecraft.massivecore.MassiveCoreMConf;
import com.massivecraft.massivecore.engine.EngineMassiveCoreTeleportMixinCause;
import com.massivecraft.massivecore.teleport.Destination;

public abstract class TeleportMixinAbstract implements TeleportMixin
{
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static int getTpdelay(Permissible delayPermissible)
	{
		return MassiveCoreMConf.get().getTpdelay(delayPermissible);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean isCausedByMixin(PlayerTeleportEvent event)
	{
		return EngineMassiveCoreTeleportMixinCause.get().isCausedByTeleportMixin(event);
	}

	@Override
	public void teleport(Object teleportee, Destination destination) throws TeleporterException
	{
		this.teleport(teleportee, destination, 0);
	}
	
	@Override
	public void teleport(Object teleportee, Destination destination, Permissible delayPermissible) throws TeleporterException
	{
		this.teleport(teleportee, destination, getTpdelay(delayPermissible));
	}
	
	// TO OVERRIDE
	/*
	@Override
	public void teleport(Object teleporteeObject, Destination destination, int delaySeconds) throws TeleporterException
	{
		// TODO Auto-generated method stub
	}
	*/
}
