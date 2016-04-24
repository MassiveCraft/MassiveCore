package com.massivecraft.massivecore.item;

import org.bukkit.block.banner.Pattern;

public class ConverterToBannerPatterns extends ConverterList<DataBannerPattern, Pattern>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterToBannerPatterns i = new ConverterToBannerPatterns();
	public static ConverterToBannerPatterns get() { return i; }
	public ConverterToBannerPatterns()
	{
		super(ConverterToBannerPattern.get());
	}

}
