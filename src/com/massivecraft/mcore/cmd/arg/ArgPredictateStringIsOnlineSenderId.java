package com.massivecraft.mcore.cmd.arg;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore.mixin.Mixin;

public class ArgPredictateStringIsOnlineSenderId implements ArgPredictate<String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ArgPredictateStringIsOnlineSenderId i = new ArgPredictateStringIsOnlineSenderId();
	public static ArgPredictateStringIsOnlineSenderId get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public boolean apply(String string, String arg, CommandSender sender)
	{
		return Mixin.isOnline(string);
	}

}
