package com.massivecraft.massivecore.store;

import com.massivecraft.massivecore.MassiveCoreMConf;

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
	public long getMillisBetweenPollColl()
	{
		return MassiveCoreMConf.get().millisBetweenLocalPollColl;
	}

	@Override
	public boolean poll(Coll<?> coll, long iterationCount)
	{
		if (iterationCount % coll.getLocalPollFrequency() == 0)
		{
			coll.identifyLocalModifications(false);
			return true;
		}
		return false;
	}

}
