package com.massivecraft.massivecore.command.type.container;

import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.command.type.Type;

import java.util.Set;

public class TypeSet<E> extends TypeContainer<Set<E>, E>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	public static <E> TypeSet<E> get(Type<E> innerType)
	{
		return new TypeSet<>(innerType);
	}
	
	public TypeSet(Type<E> innerType)
	{
		super(Set.class, innerType);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public Set<E> createNewInstance()
	{
		return new MassiveSet<>();
	}

}
