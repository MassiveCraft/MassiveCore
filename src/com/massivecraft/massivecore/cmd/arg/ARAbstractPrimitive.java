package com.massivecraft.massivecore.cmd.arg;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.util.Txt;

public abstract class ARAbstractPrimitive<T> extends ARAbstractException<T>
{	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String extractErrorMessage(String arg, CommandSender sender, Exception ex)
	{
		return Txt.parse("<b>\"<h>%s<b>\" is not a %s.", arg, this.getTypeName());
	}

}
