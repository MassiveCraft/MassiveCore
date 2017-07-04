package com.massivecraft.massivecore.command.type.enumeration;

import org.bukkit.entity.Parrot;

public class TypeVariant extends TypeEnum<Parrot.Variant>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeVariant i = new TypeVariant();
	public static TypeVariant get() { return i; }
	public TypeVariant()
	{
		super(Parrot.Variant.class);
	}
	
}
