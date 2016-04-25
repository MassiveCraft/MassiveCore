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
	public Integer getA(DataItemStack ca, ItemStack d)
	{
		return ca.getCount();
	}

	@Override
	public void setA(DataItemStack ca, Integer fa, ItemStack d)
	{
		ca.setCount(fa);
	}

	@Override
	public Integer getB(ItemStack cb, ItemStack d)
	{
		return cb.getAmount();
	}

	@Override
	public void setB(ItemStack cb, Integer fb, ItemStack d)
	{
		cb.setAmount(fb);
	}
	
}
