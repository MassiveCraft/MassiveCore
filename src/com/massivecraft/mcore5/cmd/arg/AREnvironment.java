package com.massivecraft.mcore5.cmd.arg;

import java.util.Collection;

import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;

import com.massivecraft.mcore5.util.MUtil;

public class AREnvironment extends ARAbstractSelect<Environment>
{
	@Override
	public String typename()
	{
		return "environment";
	}

	@Override
	public Environment select(String str, CommandSender sender)
	{
		Environment ret = null;
		
		// "THE_END" --> "end"
		str = str.toLowerCase();
		str = str.replace("_", "");
		str = str.replace("the", "");
		
		if (str.startsWith("no") || str.startsWith("d"))
		{
			// "normal" or "default"
			ret = Environment.NORMAL;
		}
		else if (str.startsWith("ne"))
		{
			// "nether"
			ret = Environment.NETHER;
		}
		else if (str.startsWith("e"))
		{
			// "end"
			ret = Environment.THE_END;
		}
		
		return ret;
	}

	@Override
	public Collection<String> altNames(CommandSender sender)
	{
		return MUtil.list("normal", "end", "nether");
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static AREnvironment i = new AREnvironment();
	public static AREnvironment get() { return i; }
	
}
