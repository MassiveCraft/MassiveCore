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
	public Integer getA(DataItemStack ca)
	{
		return ca.getId();
	}

	@Override
	public void setA(DataItemStack ca, Integer fa)
	{
		ca.setId(fa);
	}

	@SuppressWarnings("deprecation")
	@Override
	public Integer getB(ItemStack cb)
	{
		return cb.getTypeId();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setB(ItemStack cb, Integer fb)
	{
		cb.setTypeId(fb);
	}
	
}
