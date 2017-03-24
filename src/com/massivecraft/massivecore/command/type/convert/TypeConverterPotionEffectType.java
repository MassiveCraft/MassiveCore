package com.massivecraft.massivecore.command.type.convert;

import com.massivecraft.massivecore.command.type.TypePotionEffectType;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.item.ConverterFromPotionEffectType;
import com.massivecraft.massivecore.item.ConverterToPotionEffectType;
import org.bukkit.potion.PotionEffectType;

public class TypeConverterPotionEffectType extends TypeConverter<PotionEffectType, Integer> 
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeConverterPotionEffectType i = new TypeConverterPotionEffectType();
	public static TypeConverterPotionEffectType get() { return i; }
	
	public TypeConverterPotionEffectType()
	{
		super(TypePotionEffectType.get(), TypeInteger.get(), ConverterFromPotionEffectType.get(), ConverterToPotionEffectType.get());
	}

}
