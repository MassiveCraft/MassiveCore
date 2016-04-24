package com.massivecraft.massivecore.item;

import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.nms.NmsItemStack;


public abstract class WriterAbstractItemStack<FA, FB> extends WriterAbstract<DataItemStack, ItemStack, DataItemStack, ItemStack, FA, FB>
{
	// -------------------------------------------- //
	// CREATE
	// -------------------------------------------- //
	
	@Override
	public DataItemStack createA()
	{
		return new DataItemStack();
	}
	
	@Override
	public ItemStack createB()
	{
		return NmsItemStack.get().createItemStack();
	}
	
}
