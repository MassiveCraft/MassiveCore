package com.massivecraft.massivecore.command.type.enumeration;

import org.bukkit.Sound;

import com.massivecraft.massivecore.util.Txt;

public class TypeSound extends TypeEnum<Sound>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeSound i = new TypeSound();
	public static TypeSound get() { return i; }
	public TypeSound()
	{
		super(Sound.class);
		this.setHelp(
			Txt.parse("<aqua>https://hub.spigotmc.org/stash/projects/SPIGOT/repos/bukkit/browse/src/main/java/org/bukkit/Sound.java")
		);
	}

}
