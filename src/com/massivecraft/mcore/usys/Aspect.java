package com.massivecraft.mcore.usys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.store.Entity;
import com.massivecraft.mcore.xlib.gson.annotations.SerializedName;

public class Aspect extends Entity<Aspect, String>
{	
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	public static Aspect get(Object oid)
	{
		return AspectColl.i.get(oid);
	}
	
	// -------------------------------------------- //
	// TRANSIENT FIELDS
	// -------------------------------------------- //
	
	protected transient boolean registered = false;
	public boolean isRegistered() { return this.registered; }
	public void register() { this.registered = true; }
	
	protected transient Collection<String> desc = new ArrayList<String>();
	public Collection<String> getDesc() { return this.desc; }
	public void setDesc(Collection<String> val) { this.desc = val; }
	public void setDesc(String... val) { this.desc = Arrays.asList(val); }
	
	// -------------------------------------------- //
	// STORED FIELDS
	// -------------------------------------------- //
	
	@SerializedName("mid")
	protected String multiverseId;
	public String getMultiverseId() { return this.multiverseId; }
	public void setMultiverseId(String multiverseId) { this.multiverseId = multiverseId; }
	public Multiverse multiverse()
	{
		Multiverse ret = MultiverseColl.i.get(this.multiverseId);
		if (ret == null) ret = MultiverseColl.i.get(MCore.DEFAULT);
		return ret;
	}
	public void multiverse(Multiverse val) { this.multiverseId = val.getId(); }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public Aspect()
	{
		
	}
	
}
