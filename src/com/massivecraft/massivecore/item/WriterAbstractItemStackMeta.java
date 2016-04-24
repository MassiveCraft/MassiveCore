package com.massivecraft.massivecore.item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class WriterAbstractItemStackMeta<FA, FB> extends WriterAbstractMeta<ItemStack, ItemMeta, FA, FB>
{	
	// -------------------------------------------- //
	// MORPH
	// -------------------------------------------- //
	
	@Override
	public ItemMeta morphB(ItemStack ob)
	{
		return ob.getItemMeta();
	}
	
	// -------------------------------------------- //
	// WRITE
	// -------------------------------------------- //

	@Override
	public void writeInner(DataItemStack oa, ItemStack ob, DataItemStack ca, ItemMeta cb, boolean a2b)
	{
		super.writeInner(oa, ob, ca, cb, a2b);
		
		// Write back the meta 
		if (a2b) ob.setItemMeta(cb);
	}
	
}
