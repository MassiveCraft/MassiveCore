package com.massivecraft.massivecore.command.type.combined;

import com.massivecraft.massivecore.item.DataPotionEffect;

public class TypeDataPotionEffect extends TypeCombined<DataPotionEffect>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeDataPotionEffect i = new TypeDataPotionEffect();
	public static TypeDataPotionEffect get() { return i; }
	public TypeDataPotionEffect()
	{
		super(DataPotionEffect.class);
	}

}
