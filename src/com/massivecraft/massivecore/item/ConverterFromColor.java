package com.massivecraft.massivecore.item;

import org.bukkit.Color;

public class ConverterFromColor extends Converter<Color, Integer>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterFromColor i = new ConverterFromColor();
	public static ConverterFromColor get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Integer convert(Color x)
	{
		if (x == null) return null;
		return x.asRGB();
	}

}
