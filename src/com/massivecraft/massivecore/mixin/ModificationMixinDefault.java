package com.massivecraft.massivecore.mixin;

import com.massivecraft.massivecore.store.Coll;

public class ModificationMixinDefault extends ModificationMixinAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ModificationMixinDefault i = new ModificationMixinDefault();
	public static ModificationMixinDefault get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	@Override
	public void syncModification(Coll<?> coll, String id)
	{
		// Nothing to do here
	}

}
