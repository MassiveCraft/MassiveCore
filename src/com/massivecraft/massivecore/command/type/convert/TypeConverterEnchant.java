package com.massivecraft.massivecore.command.type.convert;

import com.massivecraft.massivecore.command.type.TypeEnchantment;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.item.ConverterFromEnchant;
import com.massivecraft.massivecore.item.ConverterToEnchant;
import org.bukkit.enchantments.Enchantment;

public class TypeConverterEnchant extends TypeConverter<Enchantment, Integer> 
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeConverterEnchant i = new TypeConverterEnchant();
	public static TypeConverterEnchant get() { return i; }
	
	public TypeConverterEnchant()
	{
		super(TypeEnchantment.get(), TypeInteger.get(), ConverterFromEnchant.get(), ConverterToEnchant.get());
	}
	
}
