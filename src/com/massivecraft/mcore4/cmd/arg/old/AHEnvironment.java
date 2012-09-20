package com.massivecraft.mcore4.cmd.arg.old;

import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;

import com.massivecraft.mcore4.MPlugin;

public class AHEnvironment extends AHBase<Environment>
{
	@Override
	public Environment parse(String str, String style, CommandSender sender, MPlugin p)
	{
		this.error.clear();
		
		Environment ret = null;
		
		// "THE_END" --> "end"
		str = str.toLowerCase();
		str = str.replace("_", "");
		str = str.replace("the", "");
		
		if (str.startsWith("no") || str.startsWith("d"))
		{
			// "normal" or "default"
			ret = Environment.NORMAL;
		}
		else if (str.startsWith("ne"))
		{
			// "nether"
			ret = Environment.NETHER;
		}
		else if (str.startsWith("e"))
		{
			// "end"
			ret = Environment.THE_END;
		}
		
		if (ret == null)
		{
			this.error.add("<b>No environment matching \"<p>"+str+"<b>\".");
			this.error.add("<i>Use <h>normal<i>, <h>end<i> or <h>nether<i>.");
		}
		
		return ret;
	}
}
