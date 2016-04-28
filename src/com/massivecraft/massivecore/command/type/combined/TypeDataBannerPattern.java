package com.massivecraft.massivecore.command.type.combined;

import com.massivecraft.massivecore.item.DataBannerPattern;

public class TypeDataBannerPattern extends TypeCombined<DataBannerPattern>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeDataBannerPattern i = new TypeDataBannerPattern();
	public static TypeDataBannerPattern get() { return i; }
	
	public TypeDataBannerPattern()
	{
		super(DataBannerPattern.class);
	}

}
