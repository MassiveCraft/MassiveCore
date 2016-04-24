package com.massivecraft.massivecore.item;

import org.bukkit.inventory.ItemFlag;

public class ConverterToItemFlags extends ConverterSet<String, ItemFlag>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterToItemFlags i = new ConverterToItemFlags();
	public static ConverterToItemFlags get() { return i; }
	public ConverterToItemFlags()
	{
		super(ConverterToItemFlag.get());
	}

}
