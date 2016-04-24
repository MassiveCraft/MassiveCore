package com.massivecraft.massivecore.item;

import org.bukkit.Color;

public class ConverterFromColors extends ConverterList<Color, Integer>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterFromColors i = new ConverterFromColors();
	public static ConverterFromColors get() { return i; }
	public ConverterFromColors()
	{
		super(ConverterFromColor.get());
	}

}
