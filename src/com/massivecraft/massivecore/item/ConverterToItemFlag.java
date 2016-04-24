package com.massivecraft.massivecore.item;

import org.bukkit.inventory.ItemFlag;

public class ConverterToItemFlag extends Converter<String, ItemFlag>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterToItemFlag i = new ConverterToItemFlag();
	public static ConverterToItemFlag get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public ItemFlag convert(String x)
	{
		if (x == null) return null;
		return ItemFlag.valueOf(x);
	}

}
