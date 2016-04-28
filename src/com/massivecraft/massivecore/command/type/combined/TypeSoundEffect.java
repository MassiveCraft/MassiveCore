package com.massivecraft.massivecore.command.type.combined;

import com.massivecraft.massivecore.SoundEffect;

public class TypeSoundEffect extends TypeCombined<SoundEffect>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final TypeSoundEffect i = new TypeSoundEffect();
	public static TypeSoundEffect get() { return i; }
	
	public TypeSoundEffect()
	{
		super(SoundEffect.class);
	}

}
