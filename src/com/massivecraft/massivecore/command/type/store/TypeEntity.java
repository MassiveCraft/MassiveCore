package com.massivecraft.massivecore.command.type.store;

import com.massivecraft.massivecore.command.type.TypeAbstractChoice;
import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.Entity;

import java.util.Collection;

public class TypeEntity<T extends Entity<T>> extends TypeAbstractChoice<T>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public static <T extends Entity<T>> TypeEntity<T> get(Coll<T> coll)
	{
		return new TypeEntity<>(coll);
	}
	
	public TypeEntity(Coll<T> coll)
	{
		super(coll.getEntityClass());
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
	public String getName()
	{
		String name = this.getColl().getClass().getSimpleName();
		name = name.substring(0, name.length() - "Coll".length());
		return name;
	}
	
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
	
	@Override
	public T createNewInstance()
	{
		return this.getColl().createNewInstance();
	}

}
