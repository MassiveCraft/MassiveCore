package com.massivecraft.mcore.usys;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.store.Coll;
import com.massivecraft.mcore.store.MStore;

public class MultiverseColl extends Coll<Multiverse>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MultiverseColl i = new MultiverseColl();
	public static MultiverseColl get() { return i; }
	private MultiverseColl()
	{
		super("mcore_multiverse", Multiverse.class, MStore.getDb("default"), MCore.get());
	}
	
	// -------------------------------------------- //
	// EXTRAS
	// -------------------------------------------- //
	
	@Override
	public void init()
	{
		super.init();
		
		// Ensure the default multiverse exits
		this.get(MCore.DEFAULT, true);
	}
	
}
