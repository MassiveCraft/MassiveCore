package com.massivecraft.massivecore;

import com.massivecraft.massivecore.store.Coll;

public class MultiverseColl extends Coll<Multiverse>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MultiverseColl i = new MultiverseColl();
	public static MultiverseColl get() { return i; }
	private MultiverseColl()
	{
		super("massivecore_multiverse");
	}

	// -------------------------------------------- //
	// STACK TRACEABILITY
	// -------------------------------------------- //
	
	@Override
	public void onTick()
	{
		super.onTick();
	}
	
	// -------------------------------------------- //
	// EXTRAS
	// -------------------------------------------- //
	
	@Override
	public void setActive(boolean active)
	{
		super.setActive(active);
		
		if ( ! active) return;
		
		// Ensure the default multiverse exits
		this.get(MassiveCore.DEFAULT, true);
	}
	
}
