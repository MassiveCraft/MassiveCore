package com.massivecraft.mcore.cmd.arg;

import java.util.Collection;

import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;

import com.massivecraft.mcore.util.MUtil;

public class ARWorldType extends ARAbstractSelect<WorldType>
{
	@Override
	public String typename()
	{
		return "world type";
	}

	@Override
	public WorldType select(String arg, CommandSender sender)
	{
		WorldType ret = null;
		
		// "DEFAULT_1_1" --> "11"
		// "LARGE_BIOMES" --> "large"
		// "Default" --> ""
		arg = arg.toLowerCase();
		arg = arg.replace("_", "");
		arg = arg.replace(".", "");
		arg = arg.replace("normal", "");
		arg = arg.replace("default", "");
		arg = arg.replace("large", "");
		
		if (arg.equals(""))
		{
			// "normal" or "default"
			ret = WorldType.NORMAL;
		}
		else if (arg.startsWith("flat"))
		{
			// "flat"
			ret = WorldType.FLAT;
		}
		else if (arg.contains("11"))
		{
			// "VERSION_1_1"
			ret = WorldType.VERSION_1_1;
		}
		else if (arg.contains("large"))
		{
			// "LARGE_BIOMES"
			ret = WorldType.LARGE_BIOMES;
		}
		
		return ret;
	}

	@Override
	public Collection<String> altNames(CommandSender sender)
	{
		return MUtil.list("normal", "flat", "1.1", "largebiomes");
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ARWorldType i = new ARWorldType();
	public static ARWorldType get() { return i; }
	
}
