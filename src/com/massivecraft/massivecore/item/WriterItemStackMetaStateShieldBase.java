package com.massivecraft.massivecore.item;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.inventory.ItemStack;

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
	public Integer getA(DataItemStack ca, ItemStack d)
	{
		return ca.getBannerBase();
	}

	@Override
	public void setA(DataItemStack ca, Integer fa, ItemStack d)
	{
		ca.setBannerBase(fa);
	}

	@Override
	public DyeColor getB(Banner cb, ItemStack d)
	{
		return cb.getBaseColor();
	}

	@Override
	public void setB(Banner cb, DyeColor fb, ItemStack d)
	{
		cb.setBaseColor(fb);
	}
	
}
