package com.massivecraft.massivecore.command.type.primitive;

import org.bukkit.command.CommandSender;

public class TypeFloat extends TypeAbstractNumber<Float>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeFloat i = new TypeFloat();
	public static TypeFloat get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getName()
	{
		return "number with decimals";
	}
	
	@Override
	public Float valueOf(String arg, CommandSender sender) throws Exception
	{
		return Float.parseFloat(arg);
	}

}
