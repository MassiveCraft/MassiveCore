package com.massivecraft.mcore4.cmd.arg.old;

import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;

import com.massivecraft.mcore4.MPlugin;


public class AHWorldType extends AHBase<WorldType>
{
	@Override
	public WorldType parse(String str, String style, CommandSender sender, MPlugin p)
	{
		this.error.clear();
		
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
		
		if (ret == null)
		{
			this.error.add("<b>No world type matching \"<p>"+str+"<b>\".");
			this.error.add("<i>Use <h>normal<i>, <h>flat<i>, <h>1.1<i> or <h>largebiomes<i>.");
		}
		
		return ret;
	}
}
