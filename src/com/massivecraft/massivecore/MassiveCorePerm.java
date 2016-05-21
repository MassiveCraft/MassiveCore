package com.massivecraft.massivecore;

import org.bukkit.permissions.Permissible;

import com.massivecraft.massivecore.util.PermissionUtil;

public enum MassiveCorePerm
{
	// -------------------------------------------- //
	// ENUM
	// -------------------------------------------- //
	
	BASECOMMAND,
	TEST,
	ID,
	VERSION,
	HEARSOUND,
	STORE,
	STORE_STATS,
	STORE_LISTCOLLS,
	STORE_COPYDB,
	USYS,
	USYS_MULTIVERSE,
	USYS_MULTIVERSE_LIST,
	USYS_MULTIVERSE_SHOW,
	USYS_MULTIVERSE_NEW,
	USYS_MULTIVERSE_DEL,
	USYS_UNIVERSE,
	USYS_UNIVERSE_NEW,
	USYS_UNIVERSE_DEL,
	USYS_UNIVERSE_CLEAR,
	USYS_WORLD,
	USYS_ASPECT,
	USYS_ASPECT_LIST,
	USYS_ASPECT_SHOW,
	USYS_ASPECT_USE,
	BUFFER,
	BUFFER_PRINT,
	BUFFER_CLEAR,
	BUFFER_SET,
	BUFFER_ADD,
	BUFFER_WHITESPACE,
	CMDURL,
	CONFIG,
	SPONSOR,
	CLICK,
	NOTPDELAY,
	VARIABLE_BOOK,
	VARIABLE_BUFFER,
	
	
	// END OF LIST
	;
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public final String node;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	MassiveCorePerm()
	{
		this.node = "massivecore." + this.name().toLowerCase().replace('_', '.');
	}
	
	// -------------------------------------------- //
	// HAS
	// -------------------------------------------- //
	
	public boolean has(Permissible permissible, boolean informSenderIfNot)
	{
		return PermissionUtil.has(permissible, this.node, informSenderIfNot);
	}
	
	public boolean has(Permissible permissible)
	{
		return has(permissible, false);
	}
}
