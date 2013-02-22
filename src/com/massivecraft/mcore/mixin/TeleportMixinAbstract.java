package com.massivecraft.mcore.mixin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

import com.massivecraft.mcore.Conf;
import com.massivecraft.mcore.PS;
import com.massivecraft.mcore.Permission;
import com.massivecraft.mcore.util.PermUtil;
import com.massivecraft.mcore.util.SenderUtil;
import com.massivecraft.mcore.util.Txt;

public abstract class TeleportMixinAbstract implements TeleportMixin
{
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void sendPreTeleportMessage(Player teleportee, String destinationDesc, int delaySeconds)
	{
		if (delaySeconds > 0)
		{
			if (destinationDesc != null)
			{
				Mixin.msg(teleportee, "<i>Teleporting to <h>"+destinationDesc+" <i>in <h>"+delaySeconds+"s <i>unless you move.");
			}
			else
			{
				Mixin.msg(teleportee, "<i>Teleporting in <h>"+delaySeconds+"s <i>unless you move.");
			}
		}
		else
		{
			if (destinationDesc != null)
			{
				Mixin.msg(teleportee, "<i>Teleporting to <h>"+destinationDesc+"<i>.");
			}
		}
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
		int delaySeconds = decideDelaySeconds(delayPermissible);
		this.teleport(teleportee, destinationPs, destinationDesc, delaySeconds);
	}

	@Override
	public void teleport(Player teleportee, PS destinationPs, String destinationDesc, Permissible delayPermissible, CommandSender otherSender, String otherPerm) throws TeleporterException
	{
		int delaySeconds = decideDelaySeconds(delayPermissible);
		this.teleport(teleportee, destinationPs, destinationDesc, delaySeconds, otherSender, otherPerm);
	}

	@Override
	public void teleport(Player teleportee, PS destinationPs, String destinationDesc, CommandSender otherSender, String otherPerm) throws TeleporterException
	{
		this.teleport(teleportee, destinationPs, destinationDesc, 0, otherSender, otherPerm);
	}

	@Override
	public void teleport(Player teleportee, PS destinationPs, String destinationDesc, int delaySeconds, CommandSender otherSender, String otherPerm) throws TeleporterException
	{
		otherPermCheck(SenderUtil.getSenderId(teleportee), otherSender, otherPerm);
		this.teleport(teleportee, destinationPs, destinationDesc, delaySeconds);
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
		int delaySeconds = decideDelaySeconds(delayPermissible);
		this.teleport(teleporteeId, destinationPs, destinationDesc, delaySeconds);
	}

	@Override
	public void teleport(String teleporteeId, PS destinationPs, String destinationDesc, Permissible delayPermissible, CommandSender otherSender, String otherPerm) throws TeleporterException
	{
		int delaySeconds = decideDelaySeconds(delayPermissible);
		this.teleport(teleporteeId, destinationPs, destinationDesc, delaySeconds, otherSender, otherPerm);
	}

	@Override
	public void teleport(String teleporteeId, PS destinationPs, String destinationDesc, CommandSender otherSender, String otherPerm) throws TeleporterException
	{
		this.teleport(teleporteeId, destinationPs, destinationDesc, 0, otherSender, otherPerm);
	}

	@Override
	public void teleport(String teleporteeId, PS destinationPs, String destinationDesc, int delaySeconds, CommandSender otherSender, String otherPerm) throws TeleporterException
	{
		otherPermCheck(teleporteeId, otherSender, otherPerm);
		this.teleport(teleporteeId, destinationPs, destinationDesc, delaySeconds);
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static int decideDelaySeconds(Permissible delayPermissible)
	{
		int ret = Conf.tpdelay;
		if (Permission.NOTPDELAY.has(delayPermissible, false))
		{
			ret = 0;
		}
		ret = Math.max(ret, 0);
		return ret;
	}
	
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
	}
	
	
	
}
