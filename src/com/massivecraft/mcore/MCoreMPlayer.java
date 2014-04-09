package com.massivecraft.mcore;

import com.massivecraft.mcore.store.Entity;

public class MCoreMPlayer extends Entity<MCoreMPlayer>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	public static MCoreMPlayer get(Object oid)
	{
		return MCoreMPlayerColl.get().get(oid);
	}

	public static MCoreMPlayer get(Object oid, boolean creative)
	{
		return MCoreMPlayerColl.get().get(oid, creative);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public MCoreMPlayer load(MCoreMPlayer that)
	{
		this.name = that.name;
		
		return this;
	}
	
	@Override
	public boolean isDefault()
	{
		if (this.name != null) return false;
		
		return true;
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private String name = null;
	public String getName() { return this.name; }
	public void setName(String name) { this.name = name; this.changed(); }
	
}
