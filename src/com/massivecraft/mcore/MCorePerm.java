package com.massivecraft.mcore;

import org.bukkit.permissions.Permissible;

import com.massivecraft.mcore.util.PermUtil;

public enum MCorePerm
{
	// -------------------------------------------- //
	// ENUM
	// -------------------------------------------- //
	
	BASECOMMAND("basecommand"),
	TEST("test"),
	ID("id"),
	VERSION("version"),
	HEARSOUND("hearsound"),
	MSTORE("mstore"),
	MSTORE_STATS("mstore.stats"),
	MSTORE_LISTCOLLS("mstore.listcolls"),
	MSTORE_COPYDB("mstore.copydb"),
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
	NOTPDELAY("notpdelay"),
	VARIABLEBOOK("variablebook"),
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
