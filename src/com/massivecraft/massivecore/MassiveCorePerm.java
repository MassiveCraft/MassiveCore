package com.massivecraft.massivecore;

import org.bukkit.permissions.Permissible;

import com.massivecraft.massivecore.util.PermUtil;

public enum MassiveCorePerm
{
	// -------------------------------------------- //
	// ENUM
	// -------------------------------------------- //
	
	BASECOMMAND("basecommand"),
	TEST("test"),
	ID("id"),
	VERSION("version"),
	HEARSOUND("hearsound"),
	STORE("store"),
	STORE_STATS("store.stats"),
	STORE_LISTCOLLS("store.listcolls"),
	STORE_COPYDB("store.copydb"),
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
	BUFFER("buffer"),
	BUFFER_PRINT("buffer.print"),
	BUFFER_CLEAR("buffer.clear"),
	BUFFER_SET("buffer.set"),
	BUFFER_ADD("buffer.add"),
	BUFFER_WHITESPACE("buffer.whitespace"),
	CMDURL("cmdurl"),
	NOTPDELAY("notpdelay"),
	VARIABLE_BOOK("variable.book"),
	VARIABLE_BUFFER("variable.buffer"),
	
	// END OF LIST
	;
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public final String node;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	MassiveCorePerm(final String permissionNode)
	{
		this.node = "massivecore."+permissionNode;
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
