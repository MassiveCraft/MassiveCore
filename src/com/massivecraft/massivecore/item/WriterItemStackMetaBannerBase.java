package com.massivecraft.massivecore.item;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.meta.BannerMeta;

public class WriterItemStackMetaBannerBase extends WriterAbstractItemMeta<BannerMeta, Integer, DyeColor>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaBannerBase i = new WriterItemStackMetaBannerBase();
	public static WriterItemStackMetaBannerBase get() { return i; }
	public WriterItemStackMetaBannerBase()
	{
		this.setMaterial(Material.BANNER);
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
	public DyeColor getB(BannerMeta cb)
	{
		return cb.getBaseColor();
	}

	@Override
	public void setB(BannerMeta cb, DyeColor fb)
	{
		cb.setBaseColor(fb);
	}
	
}
