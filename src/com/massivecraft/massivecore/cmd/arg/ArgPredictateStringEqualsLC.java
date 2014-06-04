package com.massivecraft.massivecore.cmd.arg;

import org.bukkit.command.CommandSender;

public class ArgPredictateStringEqualsLC implements ArgPredictate<String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ArgPredictateStringEqualsLC i = new ArgPredictateStringEqualsLC();
	public static ArgPredictateStringEqualsLC get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public boolean apply(String string, String arg, CommandSender sender)
	{
		return string.toLowerCase().equals(arg);
	}

}
