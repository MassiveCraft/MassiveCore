package com.massivecraft.massivecore.cmd.arg;

import org.bukkit.command.CommandSender;

public class ARInteger extends ARAbstractNumber<Integer>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARInteger i = new ARInteger();
	public static ARInteger get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getTypeName()
	{
		return "number";
	}

	@Override
	public Integer valueOf(String arg, CommandSender sender) throws Exception
	{
		return Integer.parseInt(arg);
	}

}