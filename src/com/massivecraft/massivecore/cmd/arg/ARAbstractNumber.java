package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;

public abstract class ARAbstractNumber<T extends Number> extends ARAbstractPrimitive<T>
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final List<String> TAB_LIST = Collections.singletonList("1");
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return TAB_LIST;
	}
	
}
