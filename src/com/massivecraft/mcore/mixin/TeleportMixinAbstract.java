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
	public void teleport(Player teleportee, PS to) throws TeleporterException
	{
		this.teleport(teleportee, to, null);
	}

	@Override
	public void teleport(Player teleportee, PS to, String desc) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, 0);
	}

	@Override
	public void teleport(Player teleportee, PS to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		int delaySeconds = getTpdelay(delayPermissible);
		this.teleport(teleportee, to, desc, delaySeconds);
	}
	
	@Override
	public void teleport(Player teleportee, PS to, String desc, int delaySeconds) throws TeleporterException
	{
		this.teleport(SenderUtil.getSenderId(teleportee), to, desc, delaySeconds);
	}
	
	// ----

	@Override
	public void teleport(String teleporteeId, PS to) throws TeleporterException
	{
		this.teleport(teleporteeId, to, null);
	}

	@Override
	public void teleport(String teleporteeId, PS to, String desc) throws TeleporterException
	{
		this.teleport(teleporteeId, to, desc, 0);
	}

	@Override
	public void teleport(String teleporteeId, PS to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		int delaySeconds = getTpdelay(delayPermissible);
		this.teleport(teleporteeId, to, desc, delaySeconds);
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static int getTpdelay(Permissible delayPermissible)
	{
		return MCoreConf.get().getTpdelay(delayPermissible);
	}
	
}
