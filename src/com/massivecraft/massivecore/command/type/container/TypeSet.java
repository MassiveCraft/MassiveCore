package com.massivecraft.massivecore.command.type.container;

import java.util.Set;

import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.command.type.Type;

public class TypeSet<E> extends TypeContainer<Set<E>, E>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	public static <E> TypeSet<E> get(Type<E> innerType)
	{
		return new TypeSet<E>(innerType);
	}
	
	public TypeSet(Type<E> innerType)
	{
		super(innerType);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public Set<E> createNewInstance()
	{
		return new MassiveSet<E>();
	}

}
