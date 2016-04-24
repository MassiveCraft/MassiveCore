package com.massivecraft.massivecore.item;

import org.bukkit.inventory.ItemStack;

public class WriterItemStackDamage extends WriterAbstractItemStack<Integer, Integer>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final WriterItemStackDamage i = new WriterItemStackDamage();
	public static WriterItemStackDamage get() { return i; }
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //

	@Override
	public Integer getA(DataItemStack ca)
	{
		return ca.getDamage();
	}

	@Override
	public void setA(DataItemStack ca, Integer fa)
	{
		ca.setDamage(fa);
	}

	@Override
	public Integer getB(ItemStack cb)
	{
		return Integer.valueOf(cb.getDurability());
	}

	@Override
	public void setB(ItemStack cb, Integer fb)
	{
		cb.setDurability(fb.shortValue());
	}
	
}
