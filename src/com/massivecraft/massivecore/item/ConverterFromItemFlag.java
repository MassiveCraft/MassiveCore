package com.massivecraft.massivecore.item;

import org.bukkit.inventory.ItemFlag;

public class ConverterFromItemFlag extends Converter<ItemFlag, String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterFromItemFlag i = new ConverterFromItemFlag();
	public static ConverterFromItemFlag get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String convert(ItemFlag x)
	{
		if (x == null) return null;
		return x.name();
	}

}
