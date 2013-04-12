package com.massivecraft.mcore.store.idstrategy;

import com.massivecraft.mcore.store.CollInterface;

public abstract class IdStrategyAiAbstract extends IdStrategyAbstract<String, String>
{
	//----------------------------------------------//
	// CONSTRUCT
	//----------------------------------------------//
	
	public IdStrategyAiAbstract()
	{
		super("ai", String.class, String.class);
	}

	// -------------------------------------------- //
	// OVERRIDE: IdStrategyAbstract
	// -------------------------------------------- //
	
	@Override public String localToRemote(Object local) { return (String)local; }
	@Override public String remoteToLocal(Object remote) { return (String)remote; }
	
	@Override
	public String generateAttempt(CollInterface<?, String> coll)
	{
		Integer ret = this.getNextAndUpdate(coll);
		if (ret == null) return null;
		
		return ret.toString();
	}
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract Integer getNextAndUpdate(CollInterface<?, String> coll);
	public abstract Integer getNext(CollInterface<?, String> coll);
	public abstract boolean setNext(CollInterface<?, String> coll, int next);
	
}
