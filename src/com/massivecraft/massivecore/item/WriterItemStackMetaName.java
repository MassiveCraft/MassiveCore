package com.massivecraft.massivecore.item;

import org.bukkit.inventory.meta.ItemMeta;

public class WriterItemStackMetaName extends WriterAbstractItemMeta<ItemMeta, String, String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaName i = new WriterItemStackMetaName();
	public static WriterItemStackMetaName get() { return i; }
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public String getA(DataItemStack ca)
	{
		return ca.getName();
	}

	@Override
	public void setA(DataItemStack ca, String fa)
	{
		ca.setName(fa);
	}

	@Override
	public String getB(ItemMeta cb)
	{
		return cb.getDisplayName();
	}

	@Override
	public void setB(ItemMeta cb, String fb)
	{
		cb.setDisplayName(fb);
	}
	
}
