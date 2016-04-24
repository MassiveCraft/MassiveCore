package com.massivecraft.massivecore.item;

import org.bukkit.inventory.ItemFlag;

public class ConverterFromItemFlags extends ConverterSet<ItemFlag, String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterFromItemFlags i = new ConverterFromItemFlags();
	public static ConverterFromItemFlags get() { return i; }
	public ConverterFromItemFlags()
	{
		super(ConverterFromItemFlag.get());
	}

}
