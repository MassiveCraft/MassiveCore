package com.massivecraft.massivecore.command.type.container;

import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.type.Type;

import java.util.List;

public class TypeList<E> extends TypeContainer<List<E>, E>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static <E> TypeList<E> get(Type<E> innerType)
	{
		return new TypeList<>(innerType);
	}
	
	public TypeList(Type<E> innerType)
	{
		super(List.class, innerType);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public List<E> createNewInstance()
	{
		return new MassiveList<>();
	}

}
