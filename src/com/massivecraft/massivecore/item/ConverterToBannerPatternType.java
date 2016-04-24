package com.massivecraft.massivecore.item;

import org.bukkit.block.banner.PatternType;

public class ConverterToBannerPatternType extends Converter<String, PatternType>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterToBannerPatternType i = new ConverterToBannerPatternType();
	public static ConverterToBannerPatternType get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public PatternType convert(String x)
	{
		if (x == null) return null;
		return PatternType.getByIdentifier(x);
	}

}
