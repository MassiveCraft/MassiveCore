package com.massivecraft.massivecore.cmd.arg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ARChatColor extends ARAbstractSelect<ChatColor>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARChatColor i = new ARChatColor();
	public static ARChatColor get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public ChatColor select(String arg, CommandSender sender)
	{
		arg = getComparable(arg);
		
		for (ChatColor cc : ChatColor.values())
		{
			String ccstr = getComparable(cc.name());
			if ( ! ccstr.equals(arg)) continue;
			return cc;
		}
		
		return null;
	}

	@Override
	public Collection<String> altNames(CommandSender sender)
	{
		List<String> ret = new ArrayList<String>();
		
		for (ChatColor cc : ChatColor.values())
		{
			ret.add(cc.toString() + getComparable(cc.name()));
		}
		
		return ret;
	}
	
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		List<String> ret = new ArrayList<String>();
		
		for (ChatColor cc : ChatColor.values())
		{
			ret.add(getComparable(cc.name()));
		}
		
		return ret;
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	// "DARK_RED" --> "darkred"
	// "DARK RED" --> "darkred"
	public static String getComparable(String string)
	{
		string = string.toLowerCase();
		string = string.replace("_", "");
		string = string.replace(" ", "");
		return string;
	}

}
