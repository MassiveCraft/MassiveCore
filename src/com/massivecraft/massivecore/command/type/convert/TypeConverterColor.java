package com.massivecraft.massivecore.command.type.convert;

import com.massivecraft.massivecore.command.type.TypeColor;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.item.ConverterFromColor;
import com.massivecraft.massivecore.item.ConverterToColor;
import org.bukkit.Color;

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
