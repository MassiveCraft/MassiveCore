package com.massivecraft.mcore4.cmd.arg;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.massivecraft.mcore4.cmd.MCommand;
import com.massivecraft.mcore4.util.Txt;

public class AROnlinePlayerMatch implements ArgReader<Player>
{
	@Override
	public ArgResult<Player> read(String str, MCommand mcommand)
	{
		ArgResult<Player> result = new ArgResult<Player>();
		
		List<Player> players = Bukkit.getServer().matchPlayer(str);
		if (players.size() == 1)
		{
			result.setResult(players.get(0));
		}
		else if (players.size() > 1)
		{
			List<String> names = new ArrayList<String>();
			for (Player player : players)
			{
				names.add(player.getName());
			}
			result.getErrors().add("<b>Online player matching \"<h>"+str+"<b>\" is ambigious.");
			result.getErrors().add("<b>Did you mean "+Txt.implodeCommaAndDot(names, "<h>%s", "<b>, ", " <b>or ", "<b>?"));
		}
		else if (players.size() == 0)
		{
			result.getErrors().add("<b>No online player matching \"<h>"+str+"<b\">.");
		}
		
		return result;
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static AROnlinePlayerMatch i = new AROnlinePlayerMatch();
	public static AROnlinePlayerMatch get() { return i; }
	
}
