package com.massivecraft.massivecore.item;

import org.bukkit.inventory.meta.ItemMeta;

public class WriterItemStackMetaUnbreakable extends WriterAbstractItemMeta<ItemMeta, Boolean, Boolean>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaUnbreakable i = new WriterItemStackMetaUnbreakable();
	public static WriterItemStackMetaUnbreakable get() { return i; }
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public Boolean getA(DataItemStack ca)
	{
		return ca.isUnbreakable();
	}

	@Override
	public void setA(DataItemStack ca, Boolean fa)
	{
		ca.setUnbreakable(fa);
	}

	@Override
	public Boolean getB(ItemMeta cb)
	{
		return cb.spigot().isUnbreakable();
	}

	@Override
	public void setB(ItemMeta cb, Boolean fb)
	{
		cb.spigot().setUnbreakable(fb);
	}
	
}
