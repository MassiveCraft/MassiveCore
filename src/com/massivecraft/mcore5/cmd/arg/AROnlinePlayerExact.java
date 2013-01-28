package com.massivecraft.mcore5.cmd.arg;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AROnlinePlayerExact implements ArgReader<Player>
{
	@Override
	public ArgResult<Player> read(String str, CommandSender sender)
	{
		ArgResult<Player> result = new ArgResult<Player>();
		Player player = Bukkit.getServer().getPlayerExact(str);
		result.setResult(player);
		
		if (!result.hasResult())
		{
			result.getErrors().add("<b>No online player with exact name \"<h>"+str+"<b>\".");
		}
		
		return result;
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static AROnlinePlayerExact i = new AROnlinePlayerExact();
	public static AROnlinePlayerExact get() { return i; }
	
}
