package com.massivecraft.massivecore.item;

import org.bukkit.FireworkEffect.Type;

public class ConverterToFireworkEffectType extends Converter<String, Type>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterToFireworkEffectType i = new ConverterToFireworkEffectType();
	public static ConverterToFireworkEffectType get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Type convert(String x)
	{
		if (x == null) return null;
		return Type.valueOf(x);
	}

}
