package com.massivecraft.massivecore.item;

import org.bukkit.enchantments.Enchantment;
import com.massivecraft.massivecore.command.type.TypeEnchantment;

public class ConverterToEnchant extends Converter<Integer, Enchantment>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterToEnchant i = new ConverterToEnchant();
	public static ConverterToEnchant get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@SuppressWarnings("deprecation")
	@Override
	public Enchantment convert(Integer x)
	{
		if (x == null) return null;
		return TypeEnchantment.getEnchantment[x];
	}

}
