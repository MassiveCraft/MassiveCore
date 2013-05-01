package com.massivecraft.mcore.store;

import java.util.Set;

public abstract class DbAbstract implements Db
{	
	@Override
	public Set<String> getCollnames()
	{
		return this.getDriver().getCollnames(this);
	}
}
