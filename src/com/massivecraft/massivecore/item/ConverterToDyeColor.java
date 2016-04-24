package com.massivecraft.massivecore.item;

import org.bukkit.DyeColor;

public class ConverterToDyeColor extends Converter<Integer, DyeColor>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterToDyeColor i = new ConverterToDyeColor();
	public static ConverterToDyeColor get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@SuppressWarnings("deprecation")
	@Override
	public DyeColor convert(Integer x)
	{
		if (x == null) return null;
		return DyeColor.getByDyeData(x.byteValue());
	}

}
