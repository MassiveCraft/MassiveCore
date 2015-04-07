package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.util.Txt;

public abstract class ARCollection<E extends Collection<?>> extends ARWrapper<E>
{
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		// Because we accept multiple arguments of the same type.
		// The passed arg might be more than one. We only want the latest.
		return this.getInnerArgReader().getTabList(sender, getLastArg(arg));
	}
	
	@Override
	public List<String> getTabListFiltered(CommandSender sender, String arg)
	{
		// Because we accept multiple arguments of the same type.
		// The passed arg might be more than one. We only want the latest.
		return this.getInnerArgReader().getTabListFiltered(sender, getLastArg(arg));
	}
	
	public static String getLastArg(String str)
	{
		String[] innerArgs = Txt.REGEX_WHITESPACE.split(str, -1);
		return innerArgs[innerArgs.length - 1];
	}
	
}
