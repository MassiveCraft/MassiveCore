package com.massivecraft.mcore;

import org.bukkit.permissions.Permissible;

import com.massivecraft.mcore.util.PermUtil;

public enum MCorePerm
{
	// -------------------------------------------- //
	// ENUM
	// -------------------------------------------- //
	
	CMD_MCORE("cmd.mcore"),
	CMD_MCORE_ID("cmd.mcore.id"),
	CMD_MCORE_VERSION("cmd.mcore.version"),
	CMD_MCORE_USYS("cmd.mcore.usys"),
	CMD_MCORE_USYS_MULTIVERSE("cmd.mcore.usys.multiverse"),
	CMD_MCORE_USYS_MULTIVERSE_LIST("cmd.mcore.usys.multiverse.list"),
	CMD_MCORE_USYS_MULTIVERSE_SHOW("cmd.mcore.usys.multiverse.show"),
	CMD_MCORE_USYS_MULTIVERSE_NEW("cmd.mcore.usys.multiverse.new"),
	CMD_MCORE_USYS_MULTIVERSE_DEL("cmd.mcore.usys.multiverse.del"),
	CMD_MCORE_USYS_UNIVERSE("cmd.mcore.usys.universe"),
	CMD_MCORE_USYS_UNIVERSE_NEW("cmd.mcore.usys.universe.new"),
	CMD_MCORE_USYS_UNIVERSE_DEL("cmd.mcore.usys.universe.del"),
	CMD_MCORE_USYS_UNIVERSE_CLEAR("cmd.mcore.usys.universe.clear"),
	CMD_MCORE_USYS_WORLD("cmd.mcore.usys.world"),
	CMD_MCORE_USYS_ASPECT("cmd.mcore.usys.aspect"),
	CMD_MCORE_USYS_ASPECT_LIST("cmd.mcore.usys.aspect.list"),
	CMD_MCORE_USYS_ASPECT_SHOW("cmd.mcore.usys.aspect.show"),
	CMD_MCORE_USYS_ASPECT_USE("cmd.mcore.usys.aspect.use"),
	
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
