package com.massivecraft.massivecore.item;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;

public class WriterItemStackMetaStateShieldPatterns extends WriterAbstractItemStackMetaStateField<Banner, List<DataBannerPattern>, List<Pattern>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaStateShieldPatterns i = new WriterItemStackMetaStateShieldPatterns();
	public static WriterItemStackMetaStateShieldPatterns get() { return i; }
	{
		this.setMaterial(Material.SHIELD);
		this.setConverterTo(ConverterToBannerPatterns.get());
		this.setConverterFrom(ConverterFromBannerPatterns.get());
		this.addDependencyClasses(
			WriterBannerPattern.class
		);
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //
	
	@Override
	public List<DataBannerPattern> getA(DataItemStack ca)
	{
		return ca.getBannerPatterns();
	}
	
	@Override
	public void setA(DataItemStack ca, List<DataBannerPattern> fa)
	{
		ca.setBannerPatterns(fa);
	}
	
	@Override
	public List<Pattern> getB(Banner cb)
	{
		return cb.getPatterns();
	}
	
	@Override
	public void setB(Banner cb, List<Pattern> fb)
	{
		cb.setPatterns(fb);
	}
	
}
