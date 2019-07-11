package com.massivecraft.massivecore.item;

import org.bukkit.enchantments.Enchantment;
import com.massivecraft.massivecore.command.type.TypeEnchantment;

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
		return TypeEnchantment.KEY_TO_ID.get(x.getKey().getKey());
	}

}
