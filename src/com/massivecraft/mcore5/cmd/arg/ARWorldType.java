package com.massivecraft.mcore5.cmd.arg;

import java.util.Collection;

import org.bukkit.WorldType;

import com.massivecraft.mcore5.cmd.MCommand;
import com.massivecraft.mcore5.util.MUtil;

public class ARWorldType extends ARAbstractSelect<WorldType>
{
	@Override
	public String typename()
	{
		return "world type";
	}

	@Override
	public WorldType select(String str, MCommand mcommand)
	{
		WorldType ret = null;
		
		// "DEFAULT_1_1" --> "11"
		// "LARGE_BIOMES" --> "large"
		// "Default" --> ""
		str = str.toLowerCase();
		str = str.replace("_", "");
		str = str.replace(".", "");
		str = str.replace("normal", "");
		str = str.replace("default", "");
		str = str.replace("large", "");
		
		if (str.equals(""))
		{
			// "normal" or "default"
			ret = WorldType.NORMAL;
		}
		else if (str.startsWith("flat"))
		{
			// "flat"
			ret = WorldType.FLAT;
		}
		else if (str.contains("11"))
		{
			// "VERSION_1_1"
			ret = WorldType.VERSION_1_1;
		}
		else if (str.contains("large"))
		{
			// "LARGE_BIOMES"
			ret = WorldType.LARGE_BIOMES;
		}
		
		return ret;
	}

	@Override
	public Collection<String> altNames(MCommand mcommand)
	{
		return MUtil.list("normal", "flat", "1.1", "largebiomes");
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ARWorldType i = new ARWorldType();
	public static ARWorldType get() { return i; }
	
}
