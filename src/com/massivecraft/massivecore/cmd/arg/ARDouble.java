package com.massivecraft.massivecore.cmd.arg;

import org.bukkit.command.CommandSender;

public class ARDouble extends ARAbstractNumber<Double>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARDouble i = new ARDouble();
	public static ARDouble get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getTypeName()
	{
		return "number with decimals";
	}
	
	@Override
	public Double valueOf(String arg, CommandSender sender) throws Exception
	{
		return Double.parseDouble(arg);
	}

}
