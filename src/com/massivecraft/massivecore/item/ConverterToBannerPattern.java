package com.massivecraft.massivecore.item;

import org.bukkit.block.banner.Pattern;

public class ConverterToBannerPattern extends Converter<DataBannerPattern, Pattern>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterToBannerPattern i = new ConverterToBannerPattern();
	public static ConverterToBannerPattern get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Pattern convert(DataBannerPattern x)
	{
		if (x == null) return null;
		return x.toBukkit();
	}

}
