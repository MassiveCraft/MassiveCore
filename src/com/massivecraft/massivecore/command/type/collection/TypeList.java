package com.massivecraft.massivecore.command.type.collection;

import java.util.List;

import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.type.Type;

public class TypeList<E> extends TypeCollection<List<E>, E>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static <E> TypeList<E> get(Type<E> innerType)
	{
		return new TypeList<E>(innerType);
	}
	
	public TypeList(Type<E> innerType)
	{
		super(innerType);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getCollectionTypeName()
	{
		return "List";
	}
	
	@Override
	public List<E> createNewInstance()
	{
		return new MassiveList<E>();
	}

}
