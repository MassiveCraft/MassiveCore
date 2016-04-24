package com.massivecraft.massivecore.item;

import org.bukkit.potion.PotionEffect;

public class ConverterToPotionEffect extends Converter<DataPotionEffect, PotionEffect>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterToPotionEffect i = new ConverterToPotionEffect();
	public static ConverterToPotionEffect get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public PotionEffect convert(DataPotionEffect x)
	{
		if (x == null) return null;
		return x.toBukkit();
	}

}
