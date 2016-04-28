package com.massivecraft.massivecore.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;

public class WriterItemStackMetaScaling extends WriterAbstractItemStackMetaField<MapMeta, Boolean, Boolean>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaScaling i = new WriterItemStackMetaScaling();
	public static WriterItemStackMetaScaling get() { return i; }
	public WriterItemStackMetaScaling()
	{
		super(MapMeta.class);
		this.setMaterial(Material.MAP);
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public Boolean getA(DataItemStack ca, ItemStack d)
	{
		return ca.isScaling();
	}

	@Override
	public void setA(DataItemStack ca, Boolean fa, ItemStack d)
	{
		ca.setScaling(fa);
	}

	@Override
	public Boolean getB(MapMeta cb, ItemStack d)
	{
		return cb.isScaling();
	}

	@Override
	public void setB(MapMeta cb, Boolean fb, ItemStack d)
	{
		cb.setScaling(fb);
	}
	
}
