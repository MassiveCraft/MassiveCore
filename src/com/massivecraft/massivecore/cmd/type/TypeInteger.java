package com.massivecraft.massivecore.cmd.type;

import org.bukkit.command.CommandSender;

public class TypeInteger extends TypeAbstractNumber<Integer>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeInteger i = new TypeInteger();
	public static TypeInteger get() { return i; }
	
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