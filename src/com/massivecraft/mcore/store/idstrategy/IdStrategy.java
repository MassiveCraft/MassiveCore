package com.massivecraft.mcore.store.idstrategy;

import com.massivecraft.mcore.store.CollInterface;

/**
 * The tasks of the IdStrategy:
 * 1. Generate a new free id that has not yet been used.
 * 2. Convert the id between java object and raw database data.
 * 
 * To complete the tasks the IdStrategy must be tailored for a specific database-driver.
 * Thus you will find multiple implementations with the name "ai" (auto increment).
 * There must be one implementation per driver.
 */
public interface IdStrategy
{
	// The name of the strategy (such as "auto_increment")
	public String getName();
	
	// Generate
	public String generate(CollInterface<?> coll);
}
