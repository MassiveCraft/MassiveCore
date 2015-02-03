package com.massivecraft.massivecore.cmd.arg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.util.MUtil;

public class ARBoolean extends ARAbstractPrimitive<Boolean>
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final Set<String> TRUE_OPTIONS = MUtil.set(
			"y", "ye", "yes",
			"on",
			"t", "tr", "tru", "true");
	
	public static final Set<String> FALSE_OPTIONS = MUtil.set(
			"n", "no",
			"of", "off",
			"f", "fa", "fal", "fals", "false");
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARBoolean i = new ARBoolean();
	public static ARBoolean get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getTypeName()
	{
		return "toggle";
	}
	
	@Override
	public Boolean valueOf(String arg, CommandSender sender) throws Exception
	{
		arg = arg.toLowerCase();
		
		if (TRUE_OPTIONS.contains(arg)) return Boolean.TRUE;
		if (FALSE_OPTIONS.contains(arg)) return Boolean.FALSE;
		
		throw new Exception();
	}

	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		List<String> ret = new ArrayList<String>();
		
		// Default yes and no.
		ret.add("yes");
		ret.add("no");
		
		// If it is empty we just want to show yes and no
		// else we might want to show other things.
		// We can safely add them because it is filtered.
		if ( ! arg.isEmpty())
		{
			ret.add("true");
			ret.add("false");
			ret.add("on");
			ret.add("off");
		}
		
		return ret;
	}

}
