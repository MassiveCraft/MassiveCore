package com.massivecraft.massivecore.item;

import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;


public abstract class WriterAbstractBannerPattern<FA, FB> extends WriterAbstractReflect<DataBannerPattern, Pattern, DataBannerPattern, Pattern, FA, FB>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public WriterAbstractBannerPattern(String fieldName)
	{
		super(Pattern.class, fieldName);
	}
	
	public WriterAbstractBannerPattern()
	{
		this(null); 
	}
	
	// -------------------------------------------- //
	// CREATE
	// -------------------------------------------- //
	
	@Override
	public DataBannerPattern createOA()
	{
		return new DataBannerPattern();
	}
	
	@Override
	public Pattern createOB()
	{
		return new Pattern(DyeColor.WHITE, PatternType.BASE);
	}
	
}
