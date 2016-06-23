package com.massivecraft.massivecore;

import com.massivecraft.massivecore.store.Coll;

public class MassiveCoreMSponsorInfoColl extends Coll<MassiveCoreMSponsorInfo>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MassiveCoreMSponsorInfoColl i = new MassiveCoreMSponsorInfoColl();
	public static MassiveCoreMSponsorInfoColl get() { return i; }
	private MassiveCoreMSponsorInfoColl()
	{
		super("massivecore_msponsorinfo");
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
		
		MassiveCoreMSponsorInfo.i = this.get(MassiveCore.INSTANCE, true);
	}
	
}
