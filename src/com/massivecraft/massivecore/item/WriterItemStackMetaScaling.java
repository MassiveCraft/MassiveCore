package com.massivecraft.massivecore.item;

import org.bukkit.Material;
import org.bukkit.inventory.meta.MapMeta;

public class WriterItemStackMetaScaling extends WriterAbstractItemStackMetaField<MapMeta, Boolean, Boolean>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaScaling i = new WriterItemStackMetaScaling();
	public static WriterItemStackMetaScaling get() { return i; }
	{
		this.setMaterial(Material.MAP);
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public Boolean getA(DataItemStack ca)
	{
		return ca.isScaling();
	}

	@Override
	public void setA(DataItemStack ca, Boolean fa)
	{
		ca.setScaling(fa);
	}

	@Override
	public Boolean getB(MapMeta cb)
	{
		return cb.isScaling();
	}

	@Override
	public void setB(MapMeta cb, Boolean fb)
	{
		cb.setScaling(fb);
	}
	
}
