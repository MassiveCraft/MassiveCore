package com.massivecraft.mcore5.store.idstrategy;

import java.util.Collection;

import com.massivecraft.mcore5.store.CollInterface;

public abstract class IdStrategyAbstract<L, R> implements IdStrategy<L, R>
{
	public IdStrategyAbstract(String name, Class<L> localClass, Class<R> remoteClass)
	{
		this.name = name;
		this.localClass = localClass;
		this.remoteClass = remoteClass;
	}
	
	protected String name;
	@Override public String getName() { return this.name; }
	
	protected Class<L> localClass;
	@Override public Class<L> getLocalClass() { return this.localClass; }
	
	protected Class<R> remoteClass;
	@Override public Class<R> getRemoteClass() { return this.remoteClass; }
	
	
	@Override
	public L generate(CollInterface<?, L> coll)
	{
		Collection<L> alreadyInUse = coll.getIds();
		L ret = null;
		do
		{
			ret = this.generateAttempt(coll);
			if (ret == null) return null;
		}
		while (alreadyInUse.contains(ret));
		return ret;
	}
	
	public abstract L generateAttempt(CollInterface<?, L> coll);
}
