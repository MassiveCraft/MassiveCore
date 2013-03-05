package com.massivecraft.mcore.mixin;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

import com.massivecraft.mcore.Conf;
import com.massivecraft.mcore.PS;
import com.massivecraft.mcore.util.SenderUtil;

public abstract class TeleportMixinAbstract implements TeleportMixin
{
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
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
		return Conf.getTpdelay(delayPermissible);
	}
	
	/*
	public static void otherPermCheck(String teleporteeId, CommandSender otherSender, String otherPerm) throws TeleporterException
	{
		String otherSenderId = SenderUtil.getSenderId(otherSender);
		if (otherSenderId.equalsIgnoreCase(teleporteeId)) return;
		if (PermUtil.has(otherSender, otherPerm, false)) return;
		throw new TeleporterException(PermUtil.getForbiddenMessage(otherPerm));
	}
	
	public static void validateTeleporteeId(String teleporteeId) throws TeleporterException
	{
		if (!SenderUtil.isPlayerId(teleporteeId)) throw new TeleporterException(Txt.parse("<white>%s <b>is not a player.", Mixin.getDisplayName(teleporteeId)));
		if (Mixin.isOffline(teleporteeId)) throw new TeleporterException(Txt.parse("<white>%s <b>is offline.", Mixin.getDisplayName(teleporteeId)));
	}*/
	
	
	
}
