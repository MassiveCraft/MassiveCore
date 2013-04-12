package com.massivecraft.mcore.store.idstrategy;

import java.util.Collection;

import com.massivecraft.mcore.store.CollInterface;

public abstract class IdStrategyAbstract implements IdStrategy
{
	public IdStrategyAbstract(String name)
	{
		this.name = name;
	}
	
	protected String name;
	@Override public String getName() { return this.name; }
	
	@Override
	public String generate(CollInterface<?> coll)
	{
		Collection<String> alreadyInUse = coll.getIds();
		String ret = null;
		do
		{
			ret = this.generateAttempt(coll);
			if (ret == null) return null;
		}
		while (alreadyInUse.contains(ret));
		return ret;
	}
	
	public abstract String generateAttempt(CollInterface<?> coll);
}
