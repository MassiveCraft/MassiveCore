package com.massivecraft.massivecore.item;

import org.bukkit.potion.PotionEffect;

public class ConverterToPotionEffects extends ConverterList<DataPotionEffect, PotionEffect>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterToPotionEffects i = new ConverterToPotionEffects();
	public static ConverterToPotionEffects get() { return i; }
	public ConverterToPotionEffects()
	{
		super(ConverterToPotionEffect.get());
	}

}
