package com.massivecraft.mcore;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.store.Coll;
import com.massivecraft.mcore.store.MStore;

public class MCoreConfColl extends Coll<MCoreConf>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MCoreConfColl i = new MCoreConfColl();
	public static MCoreConfColl get() { return i; }
	private MCoreConfColl()
	{
		super("mcore_conf", MCoreConf.class, MStore.getDb(ConfServer.dburi), MCore.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void init()
	{
		super.init();
		MCoreConf.i = this.get(MCore.INSTANCE, true);
	}
	
	@Override
	public synchronized void loadFromRemote(Object oid)
	{
		super.loadFromRemote(oid);
		if ( ! this.inited()) return;
	}
	
}
