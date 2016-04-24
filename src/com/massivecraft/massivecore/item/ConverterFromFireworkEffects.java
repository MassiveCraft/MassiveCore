package com.massivecraft.massivecore.item;

import org.bukkit.FireworkEffect;

public class ConverterFromFireworkEffects extends ConverterList<FireworkEffect, DataFireworkEffect>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterFromFireworkEffects i = new ConverterFromFireworkEffects();
	public static ConverterFromFireworkEffects get() { return i; }
	public ConverterFromFireworkEffects()
	{
		super(ConverterFromFireworkEffect.get());
	}

}
