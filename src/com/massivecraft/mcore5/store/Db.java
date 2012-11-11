package com.massivecraft.mcore5.store;

import java.util.Set;

public interface Db<R>
{	
	public String getName();
	
	public boolean drop();
	
	public Set<String> getCollnames();
	
	public Driver<R> getDriver();
	
	public Object getCollDriverObject(Coll<?, ?> coll);
}
