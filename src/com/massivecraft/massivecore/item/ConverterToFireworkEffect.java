package com.massivecraft.massivecore.item;

import org.bukkit.FireworkEffect;

public class ConverterToFireworkEffect extends Converter<DataFireworkEffect, FireworkEffect>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterToFireworkEffect i = new ConverterToFireworkEffect();
	public static ConverterToFireworkEffect get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public FireworkEffect convert(DataFireworkEffect x)
	{
		if (x == null) return null;
		return x.toBukkit();
	}

}
