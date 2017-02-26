package com.massivecraft.massivecore.command.type.container;

import com.massivecraft.massivecore.collections.BackstringEnumSet;
import com.massivecraft.massivecore.command.type.Type;

import java.util.Set;

public class TypeBackStringEnumSet<E extends Enum<E>> extends TypeContainer<Set<E>, E>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private Class<? extends Enum> innerTypeClass;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public static <E extends Enum<E>> TypeBackStringEnumSet<E> get(Type<E> innerType)
	{
		return new TypeBackStringEnumSet<E>(innerType);
	}
	
	public TypeBackStringEnumSet(Type<E> innerType)
	{
		super(BackstringEnumSet.class, innerType);
		this.innerTypeClass = innerType.getClazz();
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public BackstringEnumSet<E> createNewInstance()
	{
		return new BackstringEnumSet<E>((Class<E>) innerTypeClass);
	}

}
