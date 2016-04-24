package com.massivecraft.massivecore.item;

import org.bukkit.inventory.ItemStack;

public class WriterItemStackCount extends WriterAbstractItemStackField<Integer, Integer>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackCount i = new WriterItemStackCount();
	public static WriterItemStackCount get() { return i; }
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public Integer getA(DataItemStack ca)
	{
		return ca.getCount();
	}

	@Override
	public void setA(DataItemStack ca, Integer fa)
	{
		ca.setCount(fa);
	}

	@Override
	public Integer getB(ItemStack cb)
	{
		return cb.getAmount();
	}

	@Override
	public void setB(ItemStack cb, Integer fb)
	{
		cb.setAmount(fb);
	}
	
}
