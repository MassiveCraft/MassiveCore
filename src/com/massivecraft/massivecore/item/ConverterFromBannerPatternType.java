package com.massivecraft.massivecore.item;

import org.bukkit.block.banner.PatternType;

public class ConverterFromBannerPatternType extends Converter<PatternType, String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterFromBannerPatternType i = new ConverterFromBannerPatternType();
	public static ConverterFromBannerPatternType get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String convert(PatternType x)
	{
		if (x == null) return null;
		return x.getIdentifier();
	}

}
