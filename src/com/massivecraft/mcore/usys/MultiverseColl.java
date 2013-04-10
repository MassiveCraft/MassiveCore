package com.massivecraft.mcore.usys;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.store.Coll;

public class MultiverseColl extends Coll<Multiverse, String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MultiverseColl i = new MultiverseColl();
	public static MultiverseColl get() { return i; }
	private MultiverseColl()
	{
		super(MCore.get(), "ai", "mcore_multiverse", Multiverse.class, String.class, false);
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
