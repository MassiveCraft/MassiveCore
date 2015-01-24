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
	public String typename()
	{
		return "chat color";
	}

	@Override
	public ChatColor select(String arg, CommandSender sender)
	{
		ChatColor ret = null;
		
		arg = getToCompare(arg);
		
		for (ChatColor cc : ChatColor.values())
		{
			String ccstr = getToCompare(cc.name());
			if ( ! ccstr.equals(arg)) continue;
			ret = cc;
			break;
		}
		
		return ret;
	}

	@Override
	public Collection<String> altNames(CommandSender sender)
	{
		List<String> ret = new ArrayList<String>();
		
		for (ChatColor cc : ChatColor.values())
		{
			ret.add(cc.toString()+getToCompare(cc.name()));
		}
		
		return ret;
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	// "DARK_RED" --> "darkred"
	// "DARK RED" --> "darkred"
	public static String getToCompare(String str)
	{
		str = str.toLowerCase();
		str = str.replace("_", "");
		str = str.replace(" ", "");
		return str;
	}
	
}
