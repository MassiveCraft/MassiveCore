package com.massivecraft.mcore;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.store.Coll;
import com.massivecraft.mcore.store.MStore;

public class MCoreConfColl extends Coll<MCoreConf, String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MCoreConfColl i = new MCoreConfColl();
	public static MCoreConfColl get() { return i; }
	private MCoreConfColl()
	{
		super(MStore.getDb(ConfServer.dburi), MCore.get(), "ai", "mcore_conf", MCoreConf.class, String.class, true);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void init()
	{
		super.init();
		this.get(MCore.INSTANCE);
	}
	
	@Override
	public synchronized void loadFromRemote(Object oid)
	{
		super.loadFromRemote(oid);
		if ( ! this.inited()) return;
	}
	
}
