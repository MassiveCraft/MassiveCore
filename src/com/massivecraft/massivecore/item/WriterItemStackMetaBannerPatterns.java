package com.massivecraft.massivecore.item;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.meta.BannerMeta;

public class WriterItemStackMetaBannerPatterns extends WriterAbstractItemMeta<BannerMeta, List<DataBannerPattern>, List<Pattern>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaBannerPatterns i = new WriterItemStackMetaBannerPatterns();
	public static WriterItemStackMetaBannerPatterns get() { return i; }
	{
		this.setMaterial(Material.BANNER);
		this.setConverterTo(ConverterToBannerPatterns.get());
		this.setConverterFrom(ConverterFromBannerPatterns.get());
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
	public List<Pattern> getB(BannerMeta cb)
	{
		return cb.getPatterns();
	}
	
	@Override
	public void setB(BannerMeta cb, List<Pattern> fb)
	{
		cb.setPatterns(fb);
	}
	
}
