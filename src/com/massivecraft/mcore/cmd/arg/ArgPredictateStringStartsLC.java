package com.massivecraft.mcore.cmd.arg;

import org.bukkit.command.CommandSender;

public class ArgPredictateStringStartsLC implements ArgPredictate<String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ArgPredictateStringStartsLC i = new ArgPredictateStringStartsLC();
	public static ArgPredictateStringStartsLC get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public boolean apply(String string, String arg, CommandSender sender)
	{
		return string.toLowerCase().startsWith(arg);
	}

}
