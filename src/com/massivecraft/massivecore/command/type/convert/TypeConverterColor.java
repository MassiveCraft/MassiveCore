package com.massivecraft.massivecore.command.type.convert;

import org.bukkit.Color;

import com.massivecraft.massivecore.command.type.TypeColor;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.item.ConverterFromColor;
import com.massivecraft.massivecore.item.ConverterToColor;

public class TypeConverterColor extends TypeConverter<Color, Integer> 
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeConverterColor i = new TypeConverterColor();
	public static TypeConverterColor get() { return i; }
	
	public TypeConverterColor()
	{
		super(TypeColor.get(), TypeInteger.get(), ConverterFromColor.get(), ConverterToColor.get());
	}
	
}
