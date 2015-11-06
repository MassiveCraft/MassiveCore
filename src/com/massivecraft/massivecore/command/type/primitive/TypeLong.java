package com.massivecraft.massivecore.command.type.primitive;

import org.bukkit.command.CommandSender;

public class TypeLong extends TypeAbstractNumber<Long>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeLong i = new TypeLong();
	public static TypeLong get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getTypeName()
	{
		return "number";
	}

	@Override
	public Long valueOf(String arg, CommandSender sender) throws Exception
	{
		return Long.parseLong(arg);
	}

}
