package com.massivecraft.massivecore.cmd.type;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.util.Txt;

public abstract class TypeAbstractPrimitive<T> extends TypeAbstractException<T>
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
