package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;

import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.util.MUtil;

public class AREnvironment extends ARAbstractSelect<Environment>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static AREnvironment i = new AREnvironment();
	public static AREnvironment get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String typename()
	{
		return "environment";
	}

	@Override
	public Environment select(String arg, CommandSender sender)
	{
		Environment ret = null;
		
		// "THE_END" --> "end"
		arg = arg.toLowerCase();
		arg = arg.replace("_", "");
		arg = arg.replace("the", "");
		
		if (arg.startsWith("no") || arg.startsWith("d"))
		{
			// "normal" or "default"
			ret = Environment.NORMAL;
		}
		else if (arg.startsWith("ne"))
		{
			// "nether"
			ret = Environment.NETHER;
		}
		else if (arg.startsWith("e"))
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
	
}
