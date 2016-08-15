package com.massivecraft.massivecore.command.type.convert;

import org.bukkit.block.banner.PatternType;

import com.massivecraft.massivecore.command.type.enumeration.TypePatternType;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.item.ConverterFromBannerPatternType;
import com.massivecraft.massivecore.item.ConverterToBannerPatternType;

public class TypeConverterBannerPatternType extends TypeConverter<PatternType, String> 
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeConverterBannerPatternType i = new TypeConverterBannerPatternType();
	public static TypeConverterBannerPatternType get() { return i; }
	
	public TypeConverterBannerPatternType()
	{
		super(TypePatternType.get(), TypeString.get(), ConverterFromBannerPatternType.get(), ConverterToBannerPatternType.get());
	}

}
