package com.massivecraft.massivecore;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.MStore;

public class MassiveCoreMConfColl extends Coll<MassiveCoreMConf>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MassiveCoreMConfColl i = new MassiveCoreMConfColl();
	public static MassiveCoreMConfColl get() { return i; }
	private MassiveCoreMConfColl()
	{
		super("massivecore_mconf", MassiveCoreMConf.class, MStore.getDb(), MassiveCore.get());
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
