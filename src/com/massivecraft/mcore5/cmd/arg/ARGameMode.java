package com.massivecraft.mcore5.cmd.arg;

import java.util.Collection;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;

import com.massivecraft.mcore5.util.MUtil;

public class ARGameMode extends ARAbstractSelect<GameMode>
{
	@Override
	public String typename()
	{
		return "game mode";
	}

	@Override
	public GameMode select(String arg, CommandSender sender)
	{
		GameMode ret = null;
		
		arg = arg.toLowerCase();
		
		if (arg.startsWith("s"))
		{
			ret = GameMode.SURVIVAL;
		}
		else if (arg.startsWith("c"))
		{
			ret = GameMode.CREATIVE;
		}
		else if (arg.startsWith("a"))
		{
			ret = GameMode.ADVENTURE;
		}

		return ret;
	}

	@Override
	public Collection<String> altNames(CommandSender sender)
	{
		return MUtil.list("survival", "creative", "adventure");
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ARGameMode i = new ARGameMode();
	public static ARGameMode get() { return i; }
	
}
