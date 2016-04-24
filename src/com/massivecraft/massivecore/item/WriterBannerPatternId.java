package com.massivecraft.massivecore.item;

import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

public class WriterBannerPatternId extends WriterAbstractBannerPattern<String, PatternType>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterBannerPatternId i = new WriterBannerPatternId();
	public static WriterBannerPatternId get() { return i; }
	public WriterBannerPatternId()
	{
		super("pattern");
		this.setConverterTo(ConverterToBannerPatternType.get());
		this.setConverterFrom(ConverterFromBannerPatternType.get());
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //
	
	@Override
	public String getA(DataBannerPattern ca)
	{
		return ca.getId();
	}
	
	@Override
	public void setA(DataBannerPattern ca, String fa)
	{
		ca.setId(fa);
	}
	
	@Override
	public PatternType getB(Pattern cb)
	{
		return cb.getPattern();
	}
	
}
