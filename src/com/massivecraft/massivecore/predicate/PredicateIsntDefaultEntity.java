package com.massivecraft.massivecore.predicate;

import com.massivecraft.massivecore.store.Entity;

public class PredicateIsntDefaultEntity implements Predicate<Entity<?>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static PredicateIsntDefaultEntity i = new PredicateIsntDefaultEntity();
	public static PredicateIsntDefaultEntity get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public boolean apply(Entity<?> entity)
	{
		return !entity.isDefault();
	}
	
}
