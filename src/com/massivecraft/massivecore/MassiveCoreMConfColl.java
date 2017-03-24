package com.massivecraft.massivecore;

import com.massivecraft.massivecore.store.Coll;

public class MassiveCoreMConfColl extends Coll<MassiveCoreMConf>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MassiveCoreMConfColl i = new MassiveCoreMConfColl();
	public static MassiveCoreMConfColl get() { return i; }
	private MassiveCoreMConfColl()
	{
		super("massivecore_mconf");
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
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void setActive(boolean active)
	{
		super.setActive(active);
		
		if ( ! active) return;
		
		MassiveCoreMConf.i = this.get(MassiveCore.INSTANCE, true);
	}
	
}
