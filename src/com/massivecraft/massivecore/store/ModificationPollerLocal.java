package com.massivecraft.massivecore.store;

import com.massivecraft.massivecore.MassiveCoreMConf;

/*
 * This class polls for local changes in all colls.
 */
public class ModificationPollerLocal extends ModificationPollerAbstract
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ModificationPollerLocal i = null;
	public static ModificationPollerLocal get()
	{
		if (i == null || !i.isAlive()) i = new ModificationPollerLocal();
		return i;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public long getMillisBetweenPoll()
	{
		return MassiveCoreMConf.get().millisBetweenLocalPoll;
	}

	@Override
	public void poll(Coll<?> coll)
	{
		coll.identifyLocalModifications(Modification.UNKNOWN_LOG);
	}

}
