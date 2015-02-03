package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.util.MUtil;

public class ARWorldType extends ARAbstractSelect<WorldType>
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final List<String> ALT_NAMES = Collections.unmodifiableList(MUtil.list("normal", "flat", "1.1", "largebiomes"));
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARWorldType i = new ARWorldType();
	public static ARWorldType get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public WorldType select(String arg, CommandSender sender)
	{	
		arg = getComparable(arg);
		
		if (arg.equals(""))
		{
			// "normal" or "default"
			return WorldType.NORMAL;
		}
		else if (arg.startsWith("flat"))
		{
			// "flat"
			return WorldType.FLAT;
		}
		else if (arg.contains("11"))
		{
			// "VERSION_1_1"
			return WorldType.VERSION_1_1;
		}
		else if (arg.contains("large"))
		{
			// "LARGE_BIOMES"
			return WorldType.LARGE_BIOMES;
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
		return this.altNames(sender);
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static String getComparable(String string)
	{
		// "DEFAULT_1_1" --> "11"
		// "LARGE_BIOMES" --> "large"
		// "Default" --> ""
		string = string.toLowerCase();
		string = string.replace("_", "");
		string = string.replace(".", "");
		string = string.replace("normal", "");
		string = string.replace("default", "");
		string = string.replace("biomes", "");
		
		return string;
	}
	
}
