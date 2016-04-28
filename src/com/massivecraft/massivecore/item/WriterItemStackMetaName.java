package com.massivecraft.massivecore.item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WriterItemStackMetaName extends WriterAbstractItemStackMetaField<ItemMeta, String, String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaName i = new WriterItemStackMetaName();
	public static WriterItemStackMetaName get() { return i; }
	public WriterItemStackMetaName()
	{
		super(ItemMeta.class);
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public String getA(DataItemStack ca, ItemStack d)
	{
		return ca.getName();
	}

	@Override
	public void setA(DataItemStack ca, String fa, ItemStack d)
	{
		ca.setName(fa);
	}

	@Override
	public String getB(ItemMeta cb, ItemStack d)
	{
		return cb.getDisplayName();
	}

	@Override
	public void setB(ItemMeta cb, String fb, ItemStack d)
	{
		cb.setDisplayName(fb);
	}
	
}
