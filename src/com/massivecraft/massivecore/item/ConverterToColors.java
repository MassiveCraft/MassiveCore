package com.massivecraft.massivecore.item;

import org.bukkit.Color;

public class ConverterToColors extends ConverterListImmutable<Integer, Color>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterToColors i = new ConverterToColors();
	public static ConverterToColors get() { return i; }
	public ConverterToColors()
	{
		super(ConverterToColor.get());
	}

}
