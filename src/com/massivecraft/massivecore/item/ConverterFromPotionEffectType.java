package com.massivecraft.massivecore.item;

import org.bukkit.potion.PotionEffectType;

public class ConverterFromPotionEffectType extends Converter<PotionEffectType, Integer>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterFromPotionEffectType i = new ConverterFromPotionEffectType();
	public static ConverterFromPotionEffectType get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@SuppressWarnings("deprecation")
	@Override
	public Integer convert(PotionEffectType x)
	{
		if (x == null) return null;
		return x.getId();
	}

}
