package com.massivecraft.massivecore.command.type.container;

import com.massivecraft.massivecore.collections.BackstringSet;
import com.massivecraft.massivecore.command.type.Type;

import java.util.Set;

public class TypeBackstringSet<E extends Enum<E>> extends TypeContainer<Set<E>, E>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private Class<? extends Enum> innerTypeClass;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public static <E extends Enum<E>> TypeBackstringSet<E> get(Type<E> innerType)
	{
		return new TypeBackstringSet<>(innerType);
	}
	
	public TypeBackstringSet(Type<E> innerType)
	{
		super(BackstringSet.class, innerType);
		this.innerTypeClass = innerType.getClazz();
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public BackstringSet<E> createNewInstance()
	{
		return new BackstringSet<>((Class<E>) innerTypeClass);
	}

}
