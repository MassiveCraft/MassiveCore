package com.massivecraft.massivecore.item;

import org.bukkit.DyeColor;

public class ConverterFromDyeColor extends Converter<DyeColor, Integer>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterFromDyeColor i = new ConverterFromDyeColor();
	public static ConverterFromDyeColor get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@SuppressWarnings("deprecation")
	@Override
	public Integer convert(DyeColor x)
	{
		if (x == null) return null;
		return Integer.valueOf(x.getDyeData());
	}

}
