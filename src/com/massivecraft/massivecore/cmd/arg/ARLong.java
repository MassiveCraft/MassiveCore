package com.massivecraft.massivecore.cmd.arg;

import org.bukkit.command.CommandSender;

public class ARLong extends ARAbstractNumber<Long>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARLong i = new ARLong();
	public static ARLong get() { return i; }
	
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
