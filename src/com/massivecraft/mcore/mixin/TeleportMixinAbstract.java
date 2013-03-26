package com.massivecraft.mcore.mixin;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.permissions.Permissible;

import com.massivecraft.mcore.MCoreConf;
import com.massivecraft.mcore.ps.PS;
import com.massivecraft.mcore.util.SenderUtil;

public abstract class TeleportMixinAbstract implements TeleportMixin
{
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean isCausedByMixin(PlayerTeleportEvent event)
	{
		return TeleportMixinCauseEngine.get().isCausedByTeleportMixin(event);
	}
	
	@Override
	public void teleport(Player teleportee, PS destinationPs) throws TeleporterException
	{
		this.teleport(teleportee, destinationPs, null);
	}

	@Override
	public void teleport(Player teleportee, PS destinationPs, String destinationDesc) throws TeleporterException
	{
		this.teleport(teleportee, destinationPs, destinationDesc, 0);
	}

	@Override
	public void teleport(Player teleportee, PS destinationPs, String destinationDesc, Permissible delayPermissible) throws TeleporterException
	{
		int delaySeconds = getTpdelay(delayPermissible);
		this.teleport(teleportee, destinationPs, destinationDesc, delaySeconds);
	}
	
	@Override
	public void teleport(Player teleportee, PS destinationPs, String destinationDesc, int delaySeconds) throws TeleporterException
	{
		this.teleport(SenderUtil.getSenderId(teleportee), destinationPs, destinationDesc, delaySeconds);
	}
	
	// ----

	@Override
	public void teleport(String teleporteeId, PS destinationPs) throws TeleporterException
	{
		this.teleport(teleporteeId, destinationPs, null);
	}

	@Override
	public void teleport(String teleporteeId, PS destinationPs, String destinationDesc) throws TeleporterException
	{
		this.teleport(teleporteeId, destinationPs, destinationDesc, 0);
	}

	@Override
	public void teleport(String teleporteeId, PS destinationPs, String destinationDesc, Permissible delayPermissible) throws TeleporterException
	{
		int delaySeconds = getTpdelay(delayPermissible);
		this.teleport(teleporteeId, destinationPs, destinationDesc, delaySeconds);
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static int getTpdelay(Permissible delayPermissible)
	{
		return MCoreConf.get().getTpdelay(delayPermissible);
	}
	
}
