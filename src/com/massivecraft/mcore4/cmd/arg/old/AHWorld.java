package com.massivecraft.mcore4.cmd.arg.old;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import com.massivecraft.mcore4.MPlugin;

public class AHWorld extends AHBase<World>
{
	@Override
	public World parse(String str, String style, CommandSender sender, MPlugin p)
	{
		this.error.clear();
		if (str == null) return null;
		
		World ret = Bukkit.getWorld(str);
		
		if (ret == null)
		{
			this.error.add("<b>No world matching \"<p>"+str+"<b>\".");
		}
	
		return ret;
	}
}
