package com.massivecraft.massivecore.item;

import org.bukkit.Material;
import org.bukkit.inventory.meta.SkullMeta;

public class WriterItemStackMetaSkull17 extends WriterAbstractItemStackMetaField<SkullMeta, String, String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaSkull17 i = new WriterItemStackMetaSkull17();
	public static WriterItemStackMetaSkull17 get() { return i; }
	public WriterItemStackMetaSkull17()
	{
		this.setMaterial(Material.SKULL_ITEM);
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public String getA(DataItemStack ca)
	{
		return ca.getSkull();
	}

	@Override
	public void setA(DataItemStack ca, String fa)
	{
		ca.setSkull(fa);
	}

	@Override
	public String getB(SkullMeta cb)
	{
		return cb.getOwner();
	}

	@Override
	public void setB(SkullMeta cb, String fb)
	{
		cb.setOwner(fb);
	}
	
}
