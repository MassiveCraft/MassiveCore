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
import com.massivecraft.mcore.util.SenderUtil;

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
		return TeleportMixinCauseEngine.get().isCausedByTeleportMixin(event);
	}
	
	// -------------------------------------------- //
	// COMMAND SENDER
	// -------------------------------------------- //
	
	// CommandSender & PS
	@Override
	public void teleport(CommandSender teleportee, PS to) throws TeleporterException
	{
		this.teleport(teleportee, to, null);
	}
	
	@Override
	public void teleport(CommandSender teleportee, PS to, String desc) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, 0);
	}
	
	@Override
	public void teleport(CommandSender teleportee, PS to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, getTpdelay(delayPermissible));
	}
	
	@Override
	public void teleport(CommandSender teleportee, PS to, String desc, int delaySeconds) throws TeleporterException
	{
		this.teleport(SenderUtil.getSenderId(teleportee), to, desc, delaySeconds);
	}
	
	// CommandSender & CommandSender
	@Override
	public void teleport(CommandSender teleportee, CommandSender to) throws TeleporterException
	{
		this.teleport(teleportee, to, null);
	}
	
	@Override
	public void teleport(CommandSender teleportee, CommandSender to, String desc) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, 0);
	}
	
	@Override
	public void teleport(CommandSender teleportee, CommandSender to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, getTpdelay(delayPermissible));
	}
	
	@Override
	public void teleport(CommandSender teleportee, CommandSender to, String desc, int delaySeconds) throws TeleporterException
	{
		this.teleport(SenderUtil.getSenderId(teleportee), to, desc, delaySeconds);
	}
	
	// CommandSender & SenderEntity
	@Override
	public void teleport(CommandSender teleportee, SenderEntity<?> to) throws TeleporterException
	{
		this.teleport(teleportee, to, null);
	}
	
	@Override
	public void teleport(CommandSender teleportee, SenderEntity<?> to, String desc) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, 0);
	}
	
	@Override
	public void teleport(CommandSender teleportee, SenderEntity<?> to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, getTpdelay(delayPermissible));
	}
	
	@Override
	public void teleport(CommandSender teleportee, SenderEntity<?> to, String desc, int delaySeconds) throws TeleporterException
	{
		this.teleport(SenderUtil.getSenderId(teleportee), to, desc, delaySeconds);
	}
	
	// CommandSender & String
	@Override
	public void teleport(CommandSender teleportee, String to) throws TeleporterException
	{
		this.teleport(teleportee, to, null);
	}
	
	@Override
	public void teleport(CommandSender teleportee, String to, String desc) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, 0);
	}
	
	@Override
	public void teleport(CommandSender teleportee, String to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, getTpdelay(delayPermissible));
	}
	
	@Override
	public void teleport(CommandSender teleportee, String to, String desc, int delaySeconds) throws TeleporterException
	{
		this.teleport(SenderUtil.getSenderId(teleportee), to, desc, delaySeconds);
	}
	
	// CommandSender & PSGetter
	@Override
	public void teleport(CommandSender teleportee, PSGetter to) throws TeleporterException
	{
		this.teleport(teleportee, to, null);
	}
	
	@Override
	public void teleport(CommandSender teleportee, PSGetter to, String desc) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, 0);
	}
	
	@Override
	public void teleport(CommandSender teleportee, PSGetter to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, getTpdelay(delayPermissible));
	}
	
	@Override
	public void teleport(CommandSender teleportee, PSGetter to, String desc, int delaySeconds) throws TeleporterException
	{
		this.teleport(SenderUtil.getSenderId(teleportee), to, desc, delaySeconds);
	}
	
	// -------------------------------------------- //
	// SENDER ENTITY
	// -------------------------------------------- //
	
	// SenderEntity & PS
	@Override
	public void teleport(SenderEntity<?> teleportee, PS to) throws TeleporterException
	{
		this.teleport(teleportee, to, null);
	}
	
	@Override
	public void teleport(SenderEntity<?> teleportee, PS to, String desc) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, 0);
	}
	
	@Override
	public void teleport(SenderEntity<?> teleportee, PS to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, getTpdelay(delayPermissible));
	}
	
	@Override
	public void teleport(SenderEntity<?> teleportee, PS to, String desc, int delaySeconds) throws TeleporterException
	{
		this.teleport(teleportee.getId(), to, desc, delaySeconds);
	}
	
	// SenderEntity & CommandSender
	@Override
	public void teleport(SenderEntity<?> teleportee, CommandSender to) throws TeleporterException
	{
		this.teleport(teleportee, to, null);
	}
	
	@Override
	public void teleport(SenderEntity<?> teleportee, CommandSender to, String desc) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, 0);
	}
	
	@Override
	public void teleport(SenderEntity<?> teleportee, CommandSender to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, getTpdelay(delayPermissible));
	}
	
	@Override
	public void teleport(SenderEntity<?> teleportee, CommandSender to, String desc, int delaySeconds) throws TeleporterException
	{
		this.teleport(teleportee.getId(), to, desc, delaySeconds);
	}
	
	// SenderEntity & SenderEntity
	@Override
	public void teleport(SenderEntity<?> teleportee, SenderEntity<?> to) throws TeleporterException
	{
		this.teleport(teleportee, to, null);
	}
	
	@Override
	public void teleport(SenderEntity<?> teleportee, SenderEntity<?> to, String desc) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, 0);
	}
	
	@Override
	public void teleport(SenderEntity<?> teleportee, SenderEntity<?> to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, getTpdelay(delayPermissible));
	}
	
	@Override
	public void teleport(SenderEntity<?> teleportee, SenderEntity<?> to, String desc, int delaySeconds) throws TeleporterException
	{
		this.teleport(teleportee.getId(), to, desc, delaySeconds);
	}
	
	// SenderEntity & String
	@Override
	public void teleport(SenderEntity<?> teleportee, String to) throws TeleporterException
	{
		this.teleport(teleportee, to, null);
	}
	
	@Override
	public void teleport(SenderEntity<?> teleportee, String to, String desc) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, 0);
	}
	
	@Override
	public void teleport(SenderEntity<?> teleportee, String to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, getTpdelay(delayPermissible));
	}
	
	@Override
	public void teleport(SenderEntity<?> teleportee, String to, String desc, int delaySeconds) throws TeleporterException
	{
		this.teleport(teleportee.getId(), to, desc, delaySeconds);
	}
	
	// SenderEntity & PSGetter
	@Override
	public void teleport(SenderEntity<?> teleportee, PSGetter to) throws TeleporterException
	{
		this.teleport(teleportee, to, null);
	}
	
	@Override
	public void teleport(SenderEntity<?> teleportee, PSGetter to, String desc) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, 0);
	}
	
	@Override
	public void teleport(SenderEntity<?> teleportee, PSGetter to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, getTpdelay(delayPermissible));
	}
	
	@Override
	public void teleport(SenderEntity<?> teleportee, PSGetter to, String desc, int delaySeconds) throws TeleporterException
	{
		this.teleport(teleportee.getId(), to, desc, delaySeconds);
	}
	
	// -------------------------------------------- //
	// STRING
	// -------------------------------------------- //
	
	// String & PS
	@Override
	public void teleport(String teleportee, PS to) throws TeleporterException
	{
		this.teleport(teleportee, to, null);
	}
	
	@Override
	public void teleport(String teleportee, PS to, String desc) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, 0);
	}
	
	@Override
	public void teleport(String teleportee, PS to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, getTpdelay(delayPermissible));
	}
	
	@Override
	public void teleport(String teleportee, PS to, String desc, int delaySeconds) throws TeleporterException
	{
		this.teleport(teleportee, PSGetterPS.valueOf(to), desc, delaySeconds);
	}
	
	// String & CommandSender
	@Override
	public void teleport(String teleportee, CommandSender to) throws TeleporterException
	{
		this.teleport(teleportee, to, null);
	}
	
	@Override
	public void teleport(String teleportee, CommandSender to, String desc) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, 0);
	}
	
	@Override
	public void teleport(String teleportee, CommandSender to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, getTpdelay(delayPermissible));
	}
	
	@Override
	public void teleport(String teleportee, CommandSender to, String desc, int delaySeconds) throws TeleporterException
	{
		this.teleport(teleportee, PSGetterPlayer.valueOf(to), desc, delaySeconds);
	}
	
	// String & SenderEntity
	@Override
	public void teleport(String teleportee, SenderEntity<?> to) throws TeleporterException
	{
		this.teleport(teleportee, to, null);
	}
	
	@Override
	public void teleport(String teleportee, SenderEntity<?> to, String desc) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, 0);
	}
	
	@Override
	public void teleport(String teleportee, SenderEntity<?> to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, getTpdelay(delayPermissible));
	}
	
	@Override
	public void teleport(String teleportee, SenderEntity<?> to, String desc, int delaySeconds) throws TeleporterException
	{
		this.teleport(teleportee, PSGetterPlayer.valueOf(to), desc, delaySeconds);
	}
	
	// String & String
	@Override
	public void teleport(String teleportee, String to) throws TeleporterException
	{
		this.teleport(teleportee, to, null);
	}
	
	@Override
	public void teleport(String teleportee, String to, String desc) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, 0);
	}
	
	@Override
	public void teleport(String teleportee, String to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, getTpdelay(delayPermissible));
	}
	
	@Override
	public void teleport(String teleportee, String to, String desc, int delaySeconds) throws TeleporterException
	{
		this.teleport(teleportee, PSGetterPlayer.valueOf(to), desc, delaySeconds);
	}
	
	// String & PSGetter
	@Override
	public void teleport(String teleportee, PSGetter to) throws TeleporterException
	{
		this.teleport(teleportee, to, null);
	}
	
	@Override
	public void teleport(String teleportee, PSGetter to, String desc) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, 0);
	}
	
	@Override
	public void teleport(String teleportee, PSGetter to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		this.teleport(teleportee, to, desc, getTpdelay(delayPermissible));
	}
	
}
