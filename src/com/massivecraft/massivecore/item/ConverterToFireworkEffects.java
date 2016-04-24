package com.massivecraft.massivecore.item;

import org.bukkit.FireworkEffect;

public class ConverterToFireworkEffects extends ConverterList<DataFireworkEffect, FireworkEffect>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterToFireworkEffects i = new ConverterToFireworkEffects();
	public static ConverterToFireworkEffects get() { return i; }
	public ConverterToFireworkEffects()
	{
		super(ConverterToFireworkEffect.get());
	}

}
