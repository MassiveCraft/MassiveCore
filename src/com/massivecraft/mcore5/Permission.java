package com.massivecraft.mcore5;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore5.util.Perm;

public enum Permission
{
	USYS("usys"),
	USYS_MULTIVERSE("usys.multiverse"),
	USYS_MULTIVERSE_LIST("usys.multiverse.list"),
	USYS_MULTIVERSE_SHOW("usys.multiverse.show"),
	USYS_MULTIVERSE_NEW("usys.multiverse.new"),
	USYS_MULTIVERSE_DEL("usys.multiverse.del"),
	USYS_UNIVERSE("usys.universe"),
	USYS_UNIVERSE_NEW("usys.universe.new"),
	USYS_UNIVERSE_DEL("usys.universe.del"),
	USYS_UNIVERSE_CLEAR("usys.universe.clear"),
	USYS_WORLD("usys.world"),
	USYS_ASPECT("usys.aspect"),
	USYS_ASPECT_LIST("usys.aspect.list"),
	USYS_ASPECT_SHOW("usys.aspect.show"),
	USYS_ASPECT_USE("usys.aspect.use"),
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
