package com.massivecraft.massivecore.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


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
		return new ItemStack(Material.AIR);
	}
	
}
