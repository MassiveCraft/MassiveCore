package com.massivecraft.massivecore.item;

import java.util.List;

import org.bukkit.inventory.meta.ItemMeta;

public class WriterItemStackMetaLore extends WriterAbstractItemStackMetaField<ItemMeta, List<String>, List<String>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaLore i = new WriterItemStackMetaLore();
	public static WriterItemStackMetaLore get() { return i; }
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public List<String> getA(DataItemStack ca)
	{
		return ca.getLore();
	}

	@Override
	public void setA(DataItemStack ca, List<String> fa)
	{
		ca.setLore(fa);
	}

	@Override
	public List<String> getB(ItemMeta cb)
	{
		return cb.getLore();
	}

	@Override
	public void setB(ItemMeta cb, List<String> fb)
	{
		cb.setLore(fb);		
	}
	
}
