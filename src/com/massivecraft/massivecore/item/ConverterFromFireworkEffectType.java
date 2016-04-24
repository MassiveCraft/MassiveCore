package com.massivecraft.massivecore.item;

import org.bukkit.FireworkEffect.Type;

public class ConverterFromFireworkEffectType extends Converter<Type, String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterFromFireworkEffectType i = new ConverterFromFireworkEffectType();
	public static ConverterFromFireworkEffectType get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String convert(Type x)
	{
		if (x == null) return null;
		return x.name();
	}

}
