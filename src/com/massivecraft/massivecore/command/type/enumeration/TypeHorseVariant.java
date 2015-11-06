package com.massivecraft.massivecore.command.type.enumeration;

import org.bukkit.entity.Horse.Variant;

public class TypeHorseVariant extends TypeEnum<Variant>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeHorseVariant i = new TypeHorseVariant();
	public static TypeHorseVariant get() { return i; }
	public TypeHorseVariant()
	{
		super(Variant.class);
	}

}
