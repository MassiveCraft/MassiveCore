package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.Difficulty;
import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.util.MUtil;

public class ARDifficulty extends ARAbstractSelect<Difficulty>
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final List<String> ALT_NAMES = Collections.unmodifiableList(MUtil.list("peaceful", "easy", "normal", "hard"));
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARDifficulty i = new ARDifficulty();
	public static ARDifficulty get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public Difficulty select(String arg, CommandSender sender)
	{
		arg = arg.toLowerCase();

		if (arg.startsWith("p"))
		{
			return Difficulty.PEACEFUL;
		}
		else if (arg.startsWith("e"))
		{
			return Difficulty.EASY;
		}
		else if (arg.startsWith("n"))
		{
			return Difficulty.NORMAL;
		}
		else if (arg.startsWith("h"))
		{
			return Difficulty.HARD;
		}
		
		return null;
	}

	@Override
	public Collection<String> altNames(CommandSender sender)
	{
		return ALT_NAMES;
	}

	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return this.altNames(sender);
	}

}
