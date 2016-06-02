package com.massivecraft.massivecore.item;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

public class WriterItemStackMetaShieldBase extends WriterAbstractItemStackMetaField<BlockStateMeta, Integer, DyeColor>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaShieldBase i = new WriterItemStackMetaShieldBase();
	public static WriterItemStackMetaShieldBase get() { return i; }
	public WriterItemStackMetaShieldBase()
	{
		super(BlockStateMeta.class);
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
	public DyeColor getB(BlockStateMeta cb, ItemStack d)
	{
		// Get
		boolean creative = false;
		Banner banner = getBanner(cb, creative);
		if (banner == null) return null;
		
		// Return
		return banner.getBaseColor();
	}

	@Override
	public void setB(BlockStateMeta cb, DyeColor fb, ItemStack d)
	{
		// Get
		boolean creative = (fb != null);
		Banner banner = getBanner(cb, creative);
		if (banner == null) return;
		
		// Change
		banner.setBaseColor(fb);
		
		// Set
		setBanner(cb, banner);
	}
	
}
