package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;

public class ARSound extends ARAbstract<Sound>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARSound i = new ARSound();
	public static ARSound get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Sound read(String arg, CommandSender sender) throws MassiveException
	{
		Sound result = getSoundFromString(arg);
		if (result == null)
		{
			throw new MassiveException()
			.addMsg("<b>No sound matches \"<h>%s<b>\".", arg)
			.addMsg("<aqua>https://hub.spigotmc.org/stash/projects/SPIGOT/repos/bukkit/browse/src/main/java/org/bukkit/Sound.java");
		}
		return result;
	}
	
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		Set<String> ret = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		
		for (Sound sound : Sound.values())
		{
			String name = getComparable(sound);
			ret.add(name);
		}
		
		return ret;
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static Sound getSoundFromString(String str)
	{
		if (str == null) return null;
		String string1 = getComparable(str);
		for (Sound sound : Sound.values())
		{
			String compare = getComparable(sound);
			if (string1.equals(compare)) return sound;
		}
		return null;
	}
	
	public static String getComparable(Sound sound)
	{
		if (sound == null) return null;
		return getComparable(sound.name());
	}
	
	public static String getComparable(String string)
	{
		if (string == null) return null;
		string = string.toLowerCase();
		string = string.replaceAll("[^a-zA-Z0-9]", "");
		return string;
	}

}
