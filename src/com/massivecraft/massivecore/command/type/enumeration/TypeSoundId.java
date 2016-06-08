package com.massivecraft.massivecore.command.type.enumeration;

import org.bukkit.Sound;

import com.massivecraft.massivecore.command.type.TypeId;

public class TypeSoundId extends TypeId<Sound>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeSoundId i = new TypeSoundId();
	public static TypeSoundId get() { return i; }
	public TypeSoundId()
	{
		super(TypeSound.get());
	}

}
