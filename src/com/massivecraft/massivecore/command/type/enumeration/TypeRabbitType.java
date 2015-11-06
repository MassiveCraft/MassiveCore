package com.massivecraft.massivecore.command.type.enumeration;

import org.bukkit.entity.Rabbit.Type;

public class TypeRabbitType extends TypeEnum<Type>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeRabbitType i = new TypeRabbitType();
	public static TypeRabbitType get() { return i; }
	public TypeRabbitType()
	{
		super(Type.class);
	}

}
