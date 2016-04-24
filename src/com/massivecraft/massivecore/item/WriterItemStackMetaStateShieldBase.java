package com.massivecraft.massivecore.item;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;

public class WriterItemStackMetaStateShieldBase extends WriterAbstractItemStackMetaStateField<Banner, Integer, DyeColor>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaStateShieldBase i = new WriterItemStackMetaStateShieldBase();
	public static WriterItemStackMetaStateShieldBase get() { return i; }
	public WriterItemStackMetaStateShieldBase()
	{
		this.setMaterial(Material.SHIELD);
		this.setConverterTo(ConverterToDyeColor.get());
		this.setConverterFrom(ConverterFromDyeColor.get());
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public Integer getA(DataItemStack ca)
	{
		return ca.getBannerBase();
	}

	@Override
	public void setA(DataItemStack ca, Integer fa)
	{
		ca.setBannerBase(fa);
	}

	@Override
	public DyeColor getB(Banner cb)
	{
		return cb.getBaseColor();
	}

	@Override
	public void setB(Banner cb, DyeColor fb)
	{
		cb.setBaseColor(fb);
	}
	
}
