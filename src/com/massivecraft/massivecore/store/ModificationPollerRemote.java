package com.massivecraft.massivecore.store;

import com.massivecraft.massivecore.MassiveCoreMConf;

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
		return MassiveCoreMConf.get().millisBetweenRemotePoll;
	}

	@Override
	public long getMillisBetweenPollColl()
	{
		return MassiveCoreMConf.get().millisBetweenRemotePollColl;
	}

	@Override
	public boolean poll(Coll<?> coll, long iterationCount)
	{
		//TODO: This could probably be true.
		coll.identifyRemoteModifications(false);
		return true;
	}

}
