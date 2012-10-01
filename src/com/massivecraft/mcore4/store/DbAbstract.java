package com.massivecraft.mcore4.store;

import java.util.Set;

public abstract class DbAbstract<R> implements Db<R>
{	
	@Override
	public Set<String> getCollnames()
	{
		return this.getDriver().getCollnames(this);
	}
}
