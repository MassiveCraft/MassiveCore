package com.massivecraft.massivecore.item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class WriterAbstractItemStackMeta<OB, CB, FA, FB> extends WriterAbstractItemStack<OB, CB, FA, FB>
{
	// -------------------------------------------- //
	// CREATE INNER
	// -------------------------------------------- //
	
	public ItemMeta createItemMeta()
	{
		return createItemMeta(this.createItemStack());
	}
	
	public static ItemMeta createItemMeta(ItemStack itemStack)
	{
		return itemStack.getItemMeta();
	}
	
}
