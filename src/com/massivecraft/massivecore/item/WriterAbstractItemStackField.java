package com.massivecraft.massivecore.item;

import org.bukkit.inventory.ItemStack;

public abstract class WriterAbstractItemStackField<FA, FB> extends WriterAbstractItemStack<ItemStack, ItemStack, FA, FB>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public WriterAbstractItemStackField()
	{
		super(ItemStack.class);
	}
	
}
