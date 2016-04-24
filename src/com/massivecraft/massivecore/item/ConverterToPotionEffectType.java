package com.massivecraft.massivecore.item;

import org.bukkit.potion.PotionEffectType;

public class ConverterToPotionEffectType extends Converter<Integer, PotionEffectType>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterToPotionEffectType i = new ConverterToPotionEffectType();
	public static ConverterToPotionEffectType get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@SuppressWarnings("deprecation")
	@Override
	public PotionEffectType convert(Integer x)
	{
		if (x == null) return null;
		return PotionEffectType.getById(x);
	}

}
