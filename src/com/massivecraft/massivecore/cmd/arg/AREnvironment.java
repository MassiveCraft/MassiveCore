package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.util.MUtil;

public class AREnvironment extends ARAbstractSelect<Environment>
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final List<String> ALT_NAMES = Collections.unmodifiableList(MUtil.list("normal", "end", "nether"));
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static AREnvironment i = new AREnvironment();
	public static AREnvironment get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public Environment select(String arg, CommandSender sender)
	{
		// "THE_END" --> "end"
		arg = getComparable(arg);
		
		if (arg.startsWith("no") || arg.startsWith("d"))
		{
			// "normal" or "default"
			return Environment.NORMAL;
		}
		else if (arg.startsWith("ne"))
		{
			// "nether"
			return Environment.NETHER;
		}
		else if (arg.startsWith("e"))
		{
			// "end"
			return Environment.THE_END;
		}
		
		return null;
	}

	@Override
	public Collection<String> altNames(CommandSender sender)
	{
		return ALT_NAMES;
	}
	
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		Collection<String> ret = this.altNames(sender);
		
		// The_end or the_nether
		if (StringUtils.startsWithIgnoreCase(arg, "t"))
		{
			ret.add("the_end");
			ret.add("the_nether");
		}
		
		return ret;
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static String getComparable(String str)
	{
		str = str.toLowerCase();
		str = str.replace(" ", "");
		str = str.replace("_", "");
		str = str.replace("the", "");
		
		return str;
	}
	
}
