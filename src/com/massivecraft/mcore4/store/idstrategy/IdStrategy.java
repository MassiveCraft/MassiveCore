package com.massivecraft.mcore4.store.idstrategy;

import com.massivecraft.mcore4.store.CollInterface;

/**
 * The tasks of the IdStrategy:
 * 1. Generate a new free id that has not yet been used.
 * 2. Convert the id between java object and raw database data.
 * 
 * To complete the tasks the IdStrategy must be tailored for a specific database-driver.
 * Thus you will find multiple implementations with the name "ai" (auto increment).
 * There must be one implementation per driver.
 */
public interface IdStrategy<L, R>
{
	// The name of the strategy (such as "auto_increment")
	public String getName();
	
	// The id classes
	public Class<L> getLocalClass();
	public Class<R> getRemoteClass();
	
	// Convert
	public R localToRemote(Object local);
	public L remoteToLocal(Object remote);
	
	// Generate
	public L generate(CollInterface<?, L> coll);
}
