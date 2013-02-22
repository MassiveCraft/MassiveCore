package com.massivecraft.mcore.store.storeadapter;

public abstract class StoreAdapterAbstract implements StoreAdapter
{
	protected String forDriverName;
	@Override public String forDriverName() { return this.forDriverName; }
	
	public StoreAdapterAbstract(String forDriverName)
	{
		this.forDriverName = forDriverName;
	}
}
