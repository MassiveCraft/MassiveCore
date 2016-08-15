package com.massivecraft.massivecore.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
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
		super(SkullMeta.class);
		this.setMaterial(Material.SKULL_ITEM);
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public String getA(DataItemStack ca, ItemStack d)
	{
		return ca.getSkull();
	}

	@Override
	public void setA(DataItemStack ca, String fa, ItemStack d)
	{
		ca.setSkull(fa);
	}

	@Override
	public String getB(SkullMeta cb, ItemStack d)
	{
		return cb.getOwner();
	}

	@Override
	public void setB(SkullMeta cb, String fb, ItemStack d)
	{
		cb.setOwner(fb);
	}
	
}
