package com.massivecraft.mcore3.cmd.arg;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import com.massivecraft.mcore3.MPlugin;

public class AHMaterial extends AHBase<Material>
{
	@Override
	public Material parse(String str, String style, CommandSender sender, MPlugin p)
	{
		this.error.clear();
		Material ret = Material.matchMaterial(str);
		
		if (ret == null)
		{
			this.error.add("<b>No material matching \"<p>"+str+"<b>\".");
			this.error.add("<i>Suggestion: <aqua>http://www.minecraftwiki.net/wiki/Data_values");
		}
		
		return ret;
	}
}
