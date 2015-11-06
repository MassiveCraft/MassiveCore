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
		// The user specifies how often a default coll should be polled.
		// Some colls might be polled more or less frequently.
		return MassiveCoreMConf.get().millisBetweenLocalPoll / MStore.DEFAULT_LOCAL_POLL_INFREQUENCY;
	}

	@Override
	public boolean poll(Coll<?> coll, long iterationCount)
	{
		if (iterationCount % coll.getLocalPollInfrequency() == 0)
		{
			coll.identifyLocalModifications(false);
			return true;
		}
		return false;
	}

}
