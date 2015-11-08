package com.massivecraft.massivecore.command.type.store;

import java.util.Collection;

import com.massivecraft.massivecore.command.type.TypeAbstractChoice;
import com.massivecraft.massivecore.store.Coll;

public class TypeColl extends TypeAbstractChoice<Coll<?>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeColl i = new TypeColl();
	public static TypeColl get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public Collection<Coll<?>> getAll()
	{
		return Coll.getMap().values();
	}
	
	@Override
	public Coll<?> getExactMatch(String arg)
	{
		return Coll.getMap().get(arg);
	}
	
}
