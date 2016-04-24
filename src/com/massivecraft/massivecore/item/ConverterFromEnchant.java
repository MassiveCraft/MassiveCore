package com.massivecraft.massivecore.item;

import org.bukkit.enchantments.Enchantment;

public class ConverterFromEnchant extends Converter<Enchantment, Integer>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterFromEnchant i = new ConverterFromEnchant();
	public static ConverterFromEnchant get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@SuppressWarnings("deprecation")
	@Override
	public Integer convert(Enchantment x)
	{
		if (x == null) return null;
		return x.getId();
	}

}
