package com.massivecraft.massivecore.command.type.primitive;

import org.bukkit.command.CommandSender;

public class TypeShort extends TypeAbstractNumber<Short>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeShort i = new TypeShort();
	public static TypeShort get() { return i; }
	public TypeShort() { super(Short.class); }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getName()
	{
		return "number";
	}

	@Override
	public Short valueOf(String arg, CommandSender sender) throws Exception
	{
		return Short.parseShort(arg);
	}

}
