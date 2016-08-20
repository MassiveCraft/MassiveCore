package com.massivecraft.massivecore.nms;

public enum ServerType
{
	// -------------------------------------------- //
	// ENUM
	// -------------------------------------------- //
	
	BUKKIT,
	FORGE,
	
	// END OF LIST
	;
	
	// -------------------------------------------- //
	// STATIC
	// -------------------------------------------- //
		
	private static final ServerType type = determineType();
	public static ServerType get() { return type; }
	
	private static ServerType determineType()
	{
		try
		{
			// This is a class used by ALL forge based servers.
			Class.forName("cpw.mods.fml.common.FMLCommonHandler");
			return FORGE;
		}
		catch (Exception e)
		{
			return BUKKIT;
		}
	}
	
}
