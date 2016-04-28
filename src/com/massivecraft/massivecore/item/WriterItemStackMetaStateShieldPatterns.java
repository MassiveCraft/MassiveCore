package com.massivecraft.massivecore.item;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;

public class WriterItemStackMetaStateShieldPatterns extends WriterAbstractItemStackMetaStateField<Banner, List<DataBannerPattern>, List<Pattern>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaStateShieldPatterns i = new WriterItemStackMetaStateShieldPatterns();
	public static WriterItemStackMetaStateShieldPatterns get() { return i; }
	
	public WriterItemStackMetaStateShieldPatterns()
	{
		super(Banner.class);
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
	public List<DataBannerPattern> getA(DataItemStack ca, ItemStack d)
	{
		return ca.getBannerPatterns();
	}
	
	@Override
	public void setA(DataItemStack ca, List<DataBannerPattern> fa, ItemStack d)
	{
		ca.setBannerPatterns(fa);
	}
	
	@Override
	public List<Pattern> getB(Banner cb, ItemStack d)
	{
		return cb.getPatterns();
	}
	
	@Override
	public void setB(Banner cb, List<Pattern> fb, ItemStack d)
	{
		cb.setPatterns(fb);
	}
	
}
