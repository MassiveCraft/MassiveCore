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
	public Difficulty select(String str, CommandSender sender)
	{
		Difficulty ret = null;
		
		str = str.toLowerCase();

		if (str.startsWith("p"))
		{
			ret = Difficulty.PEACEFUL;
		}
		else if (str.startsWith("e"))
		{
			ret = Difficulty.EASY;
		}
		else if (str.startsWith("n"))
		{
			ret = Difficulty.NORMAL;
		}
		else if (str.startsWith("h"))
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
