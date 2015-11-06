package com.massivecraft.massivecore.command.type.enumeration;

import org.bukkit.entity.Ocelot.Type;

public class TypeOcelotType extends TypeEnum<Type>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeOcelotType i = new TypeOcelotType();
	public static TypeOcelotType get() { return i; }
	public TypeOcelotType()
	{
		super(Type.class);
	}

}
