package com.massivecraft.massivecore.command.type.collection;

import java.util.Set;

import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.command.type.Type;

public class TypeSet<E> extends TypeCollection<Set<E>, E>
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
	public String getCollectionTypeName()
	{
		return "Set";
	}

	@Override
	public Set<E> createNewInstance()
	{
		return new MassiveSet<E>();
	}

}
