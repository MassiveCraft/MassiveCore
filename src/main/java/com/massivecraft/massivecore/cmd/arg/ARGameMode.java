package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.util.MUtil;

public class ARGameMode extends ARAbstractSelect<GameMode>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARGameMode i = new ARGameMode();
	public static ARGameMode get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
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
		
		if (arg.startsWith("s") || arg.equals("0"))
		{
			ret = GameMode.SURVIVAL;
		}
		else if (arg.startsWith("c") || arg.equals("1"))
		{
			ret = GameMode.CREATIVE;
		}
		else if (arg.startsWith("a") || arg.equals("2"))
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
	
}
