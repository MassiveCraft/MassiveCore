package com.massivecraft.massivecore.command.type.store;

import java.util.Collection;

import com.massivecraft.massivecore.command.type.TypeAbstractChoice;
import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.Entity;

public class TypeEntity<T extends Entity<T>> extends TypeAbstractChoice<T>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public TypeEntity(Coll<T> coll)
	{
		this.coll = coll;
	}
	
	// -------------------------------------------- //
	// FIELD
	// -------------------------------------------- //
	
	protected final Coll<T> coll;
	public Coll<T> getColl() { return this.coll; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public T getExactMatch(String arg)
	{
		return this.getColl().get(arg);
	}
	
	@Override
	public Collection<T> getAll()
	{
		return this.getColl().getAll();
	}

}
