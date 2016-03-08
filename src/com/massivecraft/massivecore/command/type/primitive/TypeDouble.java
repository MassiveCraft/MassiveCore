package com.massivecraft.massivecore.command.type.primitive;

import org.bukkit.command.CommandSender;

public class TypeDouble extends TypeAbstractNumber<Double>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeDouble i = new TypeDouble();
	public static TypeDouble get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getName()
	{
		return "number with decimals";
	}
	
	@Override
	public Double valueOf(String arg, CommandSender sender) throws Exception
	{
		return Double.parseDouble(arg);
	}

}
