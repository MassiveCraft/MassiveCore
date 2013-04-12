package com.massivecraft.mcore.store.idstrategy;

import com.massivecraft.mcore.store.CollInterface;

public abstract class IdStrategyAiAbstract extends IdStrategyAbstract
{
	//----------------------------------------------//
	// CONSTRUCT
	//----------------------------------------------//
	
	public IdStrategyAiAbstract()
	{
		super("ai");
	}

	// -------------------------------------------- //
	// OVERRIDE: IdStrategyAbstract
	// -------------------------------------------- //
	
	@Override
	public String generateAttempt(CollInterface<?> coll)
	{
		Integer ret = this.getNextAndUpdate(coll);
		if (ret == null) return null;
		
		return ret.toString();
	}
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract Integer getNextAndUpdate(CollInterface<?> coll);
	public abstract Integer getNext(CollInterface<?> coll);
	public abstract boolean setNext(CollInterface<?> coll, int next);
	
}
