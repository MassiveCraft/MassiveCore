package com.massivecraft.massivecore.item;

import org.bukkit.potion.PotionEffect;

public class ConverterFromPotionEffects extends ConverterList<PotionEffect, DataPotionEffect>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterFromPotionEffects i = new ConverterFromPotionEffects();
	public static ConverterFromPotionEffects get() { return i; }
	public ConverterFromPotionEffects()
	{
		super(ConverterFromPotionEffect.get());
	}

}
