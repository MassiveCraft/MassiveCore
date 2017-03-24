package com.massivecraft.massivecore.command.type.store;

import com.massivecraft.massivecore.command.type.TypeAbstractChoice;
import com.massivecraft.massivecore.store.Coll;

import java.util.Collection;

public class TypeColl extends TypeAbstractChoice<Coll<?>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeColl i = new TypeColl();
	public static TypeColl get() { return i; }
	public TypeColl() { super(Coll.class); }
	
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
