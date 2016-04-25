package com.massivecraft.massivecore.item;

import org.bukkit.inventory.ItemStack;

public class WriterItemStackId extends WriterAbstractItemStackField<Integer, Integer>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackId i = new WriterItemStackId();
	public static WriterItemStackId get() { return i; }
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public Integer getA(DataItemStack ca, ItemStack d)
	{
		return ca.getId();
	}

	@Override
	public void setA(DataItemStack ca, Integer fa, ItemStack d)
	{
		ca.setId(fa);
	}

	@SuppressWarnings("deprecation")
	@Override
	public Integer getB(ItemStack cb, ItemStack d)
	{
		return cb.getTypeId();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setB(ItemStack cb, Integer fb, ItemStack d)
	{
		cb.setTypeId(fb);
	}
	
}
