package com.massivecraft.massivecore.item;

import org.bukkit.FireworkEffect;

public class ConverterFromFireworkEffect extends Converter<FireworkEffect, DataFireworkEffect>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterFromFireworkEffect i = new ConverterFromFireworkEffect();
	public static ConverterFromFireworkEffect get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public DataFireworkEffect convert(FireworkEffect x)
	{
		if (x == null) return null;
		return new DataFireworkEffect(x);
	}

}
