package com.massivecraft.mcore5.store.storeadapter;

import com.massivecraft.mcore5.store.Coll;

public interface StoreAdapter
{
	// A store adapter is supposed to be used with a certain driver.
	// This method returns the name of that driver.
	public String forDriverName();
	
	public Object read(Coll<?, ?> coll, Object entity);
	public void write(Coll<?, ?> coll, Object raw, Object entity); // (This is an opaque/complete write)
}
