package com.massivecraft.mcore1.cmd.arg;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.mcore1.plugin.MPlugin;

public class AHPlayer extends AHBase<Player>
{
	@Override
	public Player parse(String str, String style, CommandSender sender, MPlugin p)
	{
		this.error = null;
		if (str == null) return null;
		
		if (style.equals("match"))
		{
			List<Player> players = Bukkit.getServer().matchPlayer(str);
			if (players.size() > 0)
			{
				return players.get(0);
			}
			this.error = "<b>No online player's name begins with \"<p>"+str+"<b>\".";
		}
		else
		{
			Player player = Bukkit.getServer().getPlayer(str);
			if (player != null)
			{
				return player;
			}
			this.error = "<b>No player online with the exact name \"<p>"+str+"<b>\".";
		}
		return null;
	}
}
