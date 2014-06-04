package com.massivecraft.massivecore;

import com.massivecraft.massivecore.store.Entity;

public class PredictateIsntDefaultEntity implements Predictate<Entity<?>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static PredictateIsntDefaultEntity i = new PredictateIsntDefaultEntity();
	public static PredictateIsntDefaultEntity get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public boolean apply(Entity<?> entity)
	{
		return !entity.isDefault();
	}
	
}
