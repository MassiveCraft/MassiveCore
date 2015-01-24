package com.massivecraft.massivecore.cmd.arg;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.MassiveCommandException;

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
	public Sound read(String arg, CommandSender sender)
	{
		Sound result = getSoundFromString(arg);
		if (result == null)
		{
			MassiveCommandException errors = new MassiveCommandException();
			errors.addErrorMsg("<b>No sound matches \"<h>"+arg+"<b>\".");
			errors.addErrorMsg("<aqua>https://hub.spigotmc.org/stash/projects/SPIGOT/repos/bukkit/browse/src/main/java/org/bukkit/Sound.java");
			throw errors;
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
