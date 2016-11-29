package com.massivecraft.massivecore.store;

import com.massivecraft.massivecore.MassiveCoreMConf;

/*
 * This class polls for remote changes in colls.
 */
public class ModificationPollerRemote extends ModificationPollerAbstract
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ModificationPollerRemote i = null;
	public static ModificationPollerRemote get()
	{
		if (i == null || !i.isAlive()) i = new ModificationPollerRemote();
		return i;
	}
	
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public long getMillisBetweenPoll()
	{
		// We will use the default DB for this check
		if (MStore.getDb().supportsPusher())
		{
			return MassiveCoreMConf.get().millisBetweenRemotePollWithPusher;
		}
		else
		{
			return MassiveCoreMConf.get().millisBetweenRemotePollWithoutPusher;
		}

	}

	@Override
	public void poll(Coll<?> coll)
	{
		coll.identifyRemoteModifications(Modification.UNKNOWN);
	}

}
