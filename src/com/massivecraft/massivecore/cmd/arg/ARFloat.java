package com.massivecraft.massivecore.cmd.arg;

import org.bukkit.command.CommandSender;

public class ARFloat extends ARAbstractNumber<Float>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARFloat i = new ARFloat();
	public static ARFloat get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getTypeName()
	{
		return "number with decimals";
	}
	
	@Override
	public Float valueOf(String arg, CommandSender sender) throws Exception
	{
		return Float.parseFloat(arg);
	}

}
