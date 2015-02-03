package com.massivecraft.massivecore.cmd.arg;

import org.bukkit.command.CommandSender;

public class ARByte extends ARAbstractNumber<Byte>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARByte i = new ARByte();
	public static ARByte get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getTypeName()
	{
		return "small number";
	}
	
	@Override
	public Byte valueOf(String arg, CommandSender sender) throws Exception
	{
		return Byte.parseByte(arg);
	}

}
