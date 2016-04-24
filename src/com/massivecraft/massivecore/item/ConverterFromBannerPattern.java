package com.massivecraft.massivecore.item;

import org.bukkit.block.banner.Pattern;

public class ConverterFromBannerPattern extends Converter<Pattern, DataBannerPattern>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterFromBannerPattern i = new ConverterFromBannerPattern();
	public static ConverterFromBannerPattern get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public DataBannerPattern convert(Pattern x)
	{
		if (x == null) return null;
		return new DataBannerPattern(x);
	}

}
