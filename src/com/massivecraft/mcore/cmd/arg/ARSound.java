package com.massivecraft.mcore.cmd.arg;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;

public class ARSound extends ArgReaderAbstract<Sound>
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
	public ArgResult<Sound> read(String arg, CommandSender sender)
	{
		ArgResult<Sound> result = new ArgResult<Sound>(getSoundFromString(arg));
		if (!result.hasResult())
		{
			result.getErrors().add("<b>No sound matches \"<h>"+arg+"<b>\".");
			result.getErrors().add("<aqua>https://github.com/Bukkit/Bukkit/blob/master/src/main/java/org/bukkit/Sound.java");
		}
		return result;
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static Sound getSoundFromString(String string)
	{
		String string1 = getCompareString(string);
		for (Sound sound : Sound.values())
		{
			String string2 = getCompareString(sound.name());
			if (string1.equals(string2)) return sound;
		}
		return null;
	}
	
	public static String getCompareString(String string)
	{
		string = string.toLowerCase();
		string = string.replaceAll("[^a-zA-Z0-9]", "");
		return string;
	}
	
}
