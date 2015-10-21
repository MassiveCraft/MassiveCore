package com.massivecraft.massivecore.cmd.type;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.Difficulty;
import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.util.MUtil;

public class TypeDifficulty extends TypeAbstractSelect<Difficulty> implements TypeAllAble<Difficulty>
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final List<String> ALT_NAMES = Collections.unmodifiableList(MUtil.list("peaceful", "easy", "normal", "hard"));
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeDifficulty i = new TypeDifficulty();
	public static TypeDifficulty get() { return i; }
	
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
	
	@Override
	public Collection<Difficulty> getAll(CommandSender sender)
	{
		return Arrays.asList(Difficulty.values());
	}

}
