package com.massivecraft.mcore5.cmd.arg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.ChatColor;

import com.massivecraft.mcore5.cmd.MCommand;

public class ARChatColor extends ARAbstractSelect<ChatColor>
{
	@Override
	public String typename()
	{
		return "chat color";
	}

	@Override
	public ChatColor select(String str, MCommand mcommand)
	{
		ChatColor ret = null;
		
		str = getToCompare(str);
		
		for (ChatColor cc : ChatColor.values())
		{

			
			String ccstr = getToCompare(cc.name());
			if ( ! ccstr.equals(str)) continue;
			ret = cc;
			break;
		}
		
		return ret;
	}

	@Override
	public Collection<String> altNames(MCommand mcommand)
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
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ARChatColor i = new ARChatColor();
	public static ARChatColor get() { return i; }
	
}
