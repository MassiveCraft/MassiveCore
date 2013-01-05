package com.massivecraft.mcore5;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore5.util.Perm;

public enum Permission
{
	CMD_USYS("cmd.usys"),
	CMD_USYS_MULTIVERSE("cmd.usys.multiverse"),
	CMD_USYS_MULTIVERSE_LIST("cmd.usys.multiverse.list"),
	CMD_USYS_MULTIVERSE_SHOW("cmd.usys.multiverse.show"),
	CMD_USYS_MULTIVERSE_NEW("cmd.usys.multiverse.new"),
	CMD_USYS_MULTIVERSE_DEL("cmd.usys.multiverse.del"),
	CMD_USYS_UNIVERSE("cmd.usys.universe"),
	CMD_USYS_UNIVERSE_NEW("cmd.usys.universe.new"),
	CMD_USYS_UNIVERSE_DEL("cmd.usys.universe.del"),
	CMD_USYS_UNIVERSE_CLEAR("cmd.usys.universe.clear"),
	CMD_USYS_WORLD("cmd.usys.world"),
	CMD_USYS_ASPECT("cmd.usys.aspect"),
	CMD_USYS_ASPECT_LIST("cmd.usys.aspect.list"),
	CMD_USYS_ASPECT_SHOW("cmd.usys.aspect.show"),
	CMD_USYS_ASPECT_USE("cmd.usys.aspect.use"),
	;
	
	public final String node;
	
	Permission(final String permissionNode)
	{
		this.node = "mcore."+permissionNode;
	}
	
	public boolean has(CommandSender sender, boolean informSenderIfNot)
	{
		return Perm.has(sender, this.node, informSenderIfNot);
	}
	
	public boolean has(CommandSender sender)
	{
		return has(sender, false);
	}
}
