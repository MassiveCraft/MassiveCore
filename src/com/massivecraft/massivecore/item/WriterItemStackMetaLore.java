package com.massivecraft.massivecore.item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class WriterItemStackMetaLore extends WriterAbstractItemStackMetaField<ItemMeta, List<String>, List<String>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackMetaLore i = new WriterItemStackMetaLore();
	public static WriterItemStackMetaLore get() { return i; }
	public WriterItemStackMetaLore()
	{
		super(ItemMeta.class);
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public List<String> getA(DataItemStack ca, ItemStack d)
	{
		return ca.getLore();
	}

	@Override
	public void setA(DataItemStack ca, List<String> fa, ItemStack d)
	{
		ca.setLore(fa);
	}

	@Override
	public List<String> getB(ItemMeta cb, ItemStack d)
	{
		return cb.getLore();
	}

	@Override
	public void setB(ItemMeta cb, List<String> fb, ItemStack d)
	{
		cb.setLore(fb);		
	}
	
}
