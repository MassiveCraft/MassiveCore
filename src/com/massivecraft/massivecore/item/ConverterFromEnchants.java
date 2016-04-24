package com.massivecraft.massivecore.item;

import org.bukkit.enchantments.Enchantment;

public class ConverterFromEnchants extends ConverterMap<Enchantment, Integer, Integer, Integer>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterFromEnchants i = new ConverterFromEnchants();
	public static ConverterFromEnchants get() { return i; }
	public ConverterFromEnchants()
	{
		super(ConverterFromEnchant.get(), ConverterDefault.get(Integer.class, Integer.class));
	}

}
