package com.massivecraft.massivecore.command.type.combined;

import com.massivecraft.massivecore.PotionEffectWrap;

public class TypePotionEffectWrap extends TypeCombined<PotionEffectWrap>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypePotionEffectWrap i = new TypePotionEffectWrap();
	public static TypePotionEffectWrap get() { return i; }

	public TypePotionEffectWrap()
	{
		super(PotionEffectWrap.class);
	}
	
}
