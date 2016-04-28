package com.massivecraft.massivecore.command.type.convert;

import org.bukkit.FireworkEffect.Type;

import com.massivecraft.massivecore.command.type.enumeration.TypeFireworkEffectType;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.item.ConverterFromFireworkEffectType;
import com.massivecraft.massivecore.item.ConverterToFireworkEffectType;

public class TypeConverterFireworkEffectType extends TypeConverter<Type, String> 
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeConverterFireworkEffectType i = new TypeConverterFireworkEffectType();
	public static TypeConverterFireworkEffectType get() { return i; }
	
	public TypeConverterFireworkEffectType()
	{
		super(TypeFireworkEffectType.get(), TypeString.get(), ConverterFromFireworkEffectType.get(), ConverterToFireworkEffectType.get());
	}

}
