package com.massivecraft.mcore;

import org.bukkit.permissions.Permissible;

import com.massivecraft.mcore.util.PermUtil;

public enum MCorePerm
{
	// -------------------------------------------- //
	// ENUM
	// -------------------------------------------- //
	
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
	CMD_MCORE("cmd.mcore"),
	NOTPDELAY("notpdelay"),
	
	// END OF LIST
	;
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public final String node;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	MCorePerm(final String permissionNode)
	{
		this.node = "mcore."+permissionNode;
	}
	
	// -------------------------------------------- //
	// HAS
	// -------------------------------------------- //
	
	public boolean has(Permissible permissible, boolean informSenderIfNot)
	{
		return PermUtil.has(permissible, this.node, informSenderIfNot);
	}
	
	public boolean has(Permissible permissible)
	{
		return has(permissible, false);
	}
}
