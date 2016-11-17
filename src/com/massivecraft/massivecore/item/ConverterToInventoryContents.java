package com.massivecraft.massivecore.item;

import java.util.Map;

import org.bukkit.inventory.ItemStack;

public class ConverterToInventoryContents extends Converter<Map<Integer, DataItemStack>, ItemStack[]>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterToInventoryContents i = new ConverterToInventoryContents();
	public static ConverterToInventoryContents get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public ItemStack[] convert(Map<Integer, DataItemStack> x)
	{
		if (x == null) return null;
		return DataItemStack.toBukkitContents(x);
	}

}
