package com.massivecraft.massivecore.item;

import org.bukkit.potion.PotionEffect;

public class ConverterFromPotionEffect extends Converter<PotionEffect, DataPotionEffect>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterFromPotionEffect i = new ConverterFromPotionEffect();
	public static ConverterFromPotionEffect get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public DataPotionEffect convert(PotionEffect x)
	{
		if (x == null) return null;
		return new DataPotionEffect(x);
	}

}
