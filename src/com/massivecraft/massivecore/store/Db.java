package com.massivecraft.massivecore.store;

import java.util.Set;

public interface Db
{	
	public String getName();
	
	public boolean drop();
	
	public Set<String> getCollnames();
	
	public Driver getDriver();
	
	public Object getCollDriverObject(Coll<?> coll);
}