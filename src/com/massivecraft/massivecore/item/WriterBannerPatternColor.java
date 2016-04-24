package com.massivecraft.massivecore.item;

import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;

public class WriterBannerPatternColor extends WriterAbstractBannerPattern<Integer, DyeColor>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterBannerPatternColor i = new WriterBannerPatternColor();
	public static WriterBannerPatternColor get() { return i; }
	public WriterBannerPatternColor()
	{
		super("color");
		this.setConverterTo(ConverterToDyeColor.get());
		this.setConverterFrom(ConverterFromDyeColor.get());
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //
	
	@Override
	public Integer getA(DataBannerPattern ca)
	{
		return ca.getColor();
	}
	
	@Override
	public void setA(DataBannerPattern ca, Integer fa)
	{
		ca.setColor(fa);
	}
	
	@Override
	public DyeColor getB(Pattern cb)
	{
		return cb.getColor();
	}
	
}
