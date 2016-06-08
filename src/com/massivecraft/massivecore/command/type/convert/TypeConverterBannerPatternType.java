package com.massivecraft.massivecore.command.type.convert;

import com.massivecraft.massivecore.command.type.Type;
import com.massivecraft.massivecore.command.type.enumeration.TypePatternType;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.item.Converter;
import com.massivecraft.massivecore.item.ConverterFromBannerPatternType;
import com.massivecraft.massivecore.item.ConverterToBannerPatternType;

// Minecraft 1.7 Compatibility
public class TypeConverterBannerPatternType<A> extends TypeConverter<A, String> 
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeConverterBannerPatternType<?> i = null;
	@SuppressWarnings("unchecked")
	public static <T> TypeConverterBannerPatternType<T> get()
	{
		if (i == null) i = new TypeConverterBannerPatternType<>(TypePatternType.get(), ConverterFromBannerPatternType.get(), ConverterToBannerPatternType.get());
		return (TypeConverterBannerPatternType<T>) i;
	}
	
	public TypeConverterBannerPatternType(Type<A> typeA, Converter<A, String> a2b, Converter<String, A> b2a)
	{
		super(typeA, TypeString.get(), a2b, b2a);
	}

}
