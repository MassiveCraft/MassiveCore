package com.massivecraft.mcore.store.idstrategy;

import com.massivecraft.mcore.store.CollInterface;

public interface IdStrategy
{
	// The name of the strategy (such as "uuid")
	public String getName();
	
	// Generate
	public String generate(CollInterface<?> coll);
}
