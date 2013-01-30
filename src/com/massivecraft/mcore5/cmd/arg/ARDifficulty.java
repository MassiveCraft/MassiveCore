package com.massivecraft.mcore5.cmd.arg;

import java.util.Collection;

import org.bukkit.Difficulty;
import org.bukkit.command.CommandSender;

import com.massivecraft.mcore5.util.MUtil;

public class ARDifficulty extends ARAbstractSelect<Difficulty>
{
	@Override
	public String typename()
	{
		return "difficulty";
	}

	@Override
	public Difficulty select(String arg, CommandSender sender)
	{
		Difficulty ret = null;
		
		arg = arg.toLowerCase();

		if (arg.startsWith("p"))
		{
			ret = Difficulty.PEACEFUL;
		}
		else if (arg.startsWith("e"))
		{
			ret = Difficulty.EASY;
		}
		else if (arg.startsWith("n"))
		{
			ret = Difficulty.NORMAL;
		}
		else if (arg.startsWith("h"))
		{
			ret = Difficulty.HARD;
		}
		
		return ret;
	}

	@Override
	public Collection<String> altNames(CommandSender sender)
	{
		return MUtil.list("peaceful", "easy", "normal", "hard");
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ARDifficulty i = new ARDifficulty();
	public static ARDifficulty get() { return i; }
	
}
