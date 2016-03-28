package com.massivecraft.massivecore.mixin;

import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.Entity;

public class MixinModification extends MixinAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MixinModification d = new MixinModification();
	private static MixinModification i = d;
	public static MixinModification get() { return i; }
	public static void set(MixinModification i) { MixinModification.i = i; }
	
	// -------------------------------------------- //
	// METHODS
	// -------------------------------------------- //
	
	public void syncModification(Entity<?> entity)
	{
		this.syncModification(entity.getColl(), entity.getId());
	}
	
	public void syncModification(Coll<?> coll, String id)
	{
		// Nothing to do here
	}

}
