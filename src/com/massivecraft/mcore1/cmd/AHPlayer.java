package com.massivecraft.mcore1.cmd;

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
			this.error = "<b>\"<p>"+str+"<b>\" did not match any online player.";
		}
		
		Player player = Bukkit.getServer().getPlayer(str);
		if (player == null)
		{
			this.error = "<b>No player online with the exact name \"<p>"+str+"<b>\".";
		}
		return player; 
	}
}
