package com.massivecraft.mcore5.cmd.arg;

import java.util.Collection;

import org.bukkit.GameMode;

import com.massivecraft.mcore5.cmd.MCommand;
import com.massivecraft.mcore5.util.MUtil;

public class ARGameMode extends ARAbstractSelect<GameMode>
{
	@Override
	public String typename()
	{
		return "game mode";
	}

	@Override
	public GameMode select(String str, MCommand mcommand)
	{
		GameMode ret = null;
		
		str = str.toLowerCase();
		
		if (str.startsWith("s"))
		{
			ret = GameMode.SURVIVAL;
		}
		else if (str.startsWith("c"))
		{
			ret = GameMode.CREATIVE;
		}
		else if (str.startsWith("a"))
		{
			ret = GameMode.ADVENTURE;
		}

		return ret;
	}

	@Override
	public Collection<String> altNames(MCommand mcommand)
	{
		return MUtil.list("survival", "creative", "adventure");
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ARGameMode i = new ARGameMode();
	public static ARGameMode get() { return i; }
	
}
