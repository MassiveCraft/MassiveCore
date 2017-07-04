package com.massivecraft.massivecore.command.type.enumeration;

import org.bukkit.entity.Parrot;

public class TypeParrotVariant extends TypeEnum<Parrot.Variant>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeParrotVariant i = new TypeParrotVariant();
	public static TypeParrotVariant get() { return i; }
	public TypeParrotVariant()
	{
		super(Parrot.Variant.class);
	}
	
}
