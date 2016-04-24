package com.massivecraft.massivecore.item;

import org.bukkit.block.banner.Pattern;

public class ConverterFromBannerPatterns extends ConverterList<Pattern, DataBannerPattern>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterFromBannerPatterns i = new ConverterFromBannerPatterns();
	public static ConverterFromBannerPatterns get() { return i; }
	public ConverterFromBannerPatterns()
	{
		super(ConverterFromBannerPattern.get());
	}

}
