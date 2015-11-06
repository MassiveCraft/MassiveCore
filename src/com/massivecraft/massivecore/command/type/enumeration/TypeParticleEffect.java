package com.massivecraft.massivecore.command.type.enumeration;

import com.massivecraft.massivecore.particleeffect.ParticleEffect;

public class TypeParticleEffect extends TypeEnum<ParticleEffect>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeParticleEffect i = new TypeParticleEffect();
	public static TypeParticleEffect get() { return i; }
	public TypeParticleEffect()
	{
		super(ParticleEffect.class);
	}

}
