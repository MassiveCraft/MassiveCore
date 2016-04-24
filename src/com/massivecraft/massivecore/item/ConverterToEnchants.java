package com.massivecraft.massivecore.item;

import org.bukkit.enchantments.Enchantment;

public class ConverterToEnchants extends ConverterMap<Integer, Integer, Enchantment, Integer>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterToEnchants i = new ConverterToEnchants();
	public static ConverterToEnchants get() { return i; }
	public ConverterToEnchants()
	{
		super(ConverterToEnchant.get(), ConverterDefault.get(Integer.class, Integer.class));
	}

}
