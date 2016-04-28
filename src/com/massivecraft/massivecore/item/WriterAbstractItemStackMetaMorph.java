package com.massivecraft.massivecore.item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class WriterAbstractItemStackMetaMorph<FA, FB> extends WriterAbstractItemStackMeta<ItemStack, ItemMeta, FA, FB>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public WriterAbstractItemStackMetaMorph()
	{
		super(ItemMeta.class);
	}
	
	// -------------------------------------------- //
	// MORPH
	// -------------------------------------------- //
	
	@Override
	public ItemMeta morphB(ItemStack ob)
	{
		return createItemMeta(ob);
	}
	
	// -------------------------------------------- //
	// WRITE
	// -------------------------------------------- //

	@Override
	public void writeInner(DataItemStack oa, ItemStack ob, DataItemStack ca, ItemMeta cb, ItemStack d, boolean a2b)
	{
		super.writeInner(oa, ob, ca, cb, d, a2b);
		
		// Write Back 
		if (a2b) ob.setItemMeta(cb);
	}
	
}
