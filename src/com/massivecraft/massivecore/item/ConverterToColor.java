package com.massivecraft.massivecore.item;

import org.bukkit.Color;

public class ConverterToColor extends Converter<Integer, Color>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterToColor i = new ConverterToColor();
	public static ConverterToColor get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Color convert(Integer x)
	{
		if (x == null) return null;
		return Color.fromRGB(x);
	}

}
