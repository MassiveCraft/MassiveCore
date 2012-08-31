package com.massivecraft.mcore4.store;

import java.util.Set;

public interface Db<R>
{	
	public String name();
	
	public boolean drop();
	
	public Set<String> collnames();
	
	public Driver<R> driver();
	
	public Object getCollDriverObject(Coll<?, ?> coll);
}
