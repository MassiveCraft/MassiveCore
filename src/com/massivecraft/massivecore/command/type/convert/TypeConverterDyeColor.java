package com.massivecraft.massivecore.command.type.convert;

import org.bukkit.DyeColor;

import com.massivecraft.massivecore.command.type.enumeration.TypeDyeColor;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.item.ConverterFromDyeColor;
import com.massivecraft.massivecore.item.ConverterToDyeColor;

public class TypeConverterDyeColor extends TypeConverter<DyeColor, Integer> 
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeConverterDyeColor i = new TypeConverterDyeColor();
	public static TypeConverterDyeColor get() { return i; }
	
	public TypeConverterDyeColor()
	{
		super(TypeDyeColor.get(), TypeInteger.get(), ConverterFromDyeColor.get(), ConverterToDyeColor.get());
	}
	
}
