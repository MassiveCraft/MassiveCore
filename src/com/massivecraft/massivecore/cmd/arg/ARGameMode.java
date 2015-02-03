package com.massivecraft.massivecore.cmd.arg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.util.Txt;

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
	public GameMode select(String arg, CommandSender sender)
	{
		arg = getComparable(arg);
		
		if (arg.length() < 2) return null; // Some gamemodes have the same beginning character. So we need atleast 2 characters.
		
		for (GameMode gm : GameMode.values())
		{
			// Comparable
			String compare = getComparable(gm);
			
			if (compare.startsWith(arg)) return gm;
		}

		return null;
	}
	
	@Override
	public Collection<String> altNames(CommandSender sender)
	{
		List<String> ret = new ArrayList<String>();
		for (GameMode gm : GameMode.values())
		{
			ret.add(Txt.getNicedEnum(gm));
		}
		return ret;
	}
	
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return this.altNames(sender);
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //

	public static String getComparable(GameMode gamemode)
	{
		if (gamemode == null) return null;
		return getComparable(gamemode.name());
	}
	
	public static String getComparable(String string)
	{
		if (string == null) return null;
		return string.toLowerCase();
	}

}
