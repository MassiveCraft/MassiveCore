package com.massivecraft.mcore2.cmd.arg;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.mcore2.MPlugin;

public class AHPlayer extends AHBase<Player>
{
	@Override
	public Player parse(String str, String style, CommandSender sender, MPlugin p)
	{
		this.error.clear();
		if (str == null) return null;
		
		if (style != null && style.equals("match"))
		{
			List<Player> players = Bukkit.getServer().matchPlayer(str);
			if (players.size() > 0)
			{
				return players.get(0);
			}
			this.error.add("<b>No online player's name begins with \"<p>"+str+"<b>\".");
		}
		else
		{
			Player player = Bukkit.getServer().getPlayer(str);
			if (player != null)
			{
				return player;
			}
			this.error.add("<b>No player online with the exact name \"<p>"+str+"<b>\".");
		}
		return null;
	}
}
