package com.massivecraft.massivecore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.google.gson.annotations.SerializedName;
import com.massivecraft.massivecore.store.Entity;

public class Aspect extends Entity<Aspect>
{	
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	public static Aspect get(Object oid)
	{
		return AspectColl.get().get(oid);
	}
	
	// -------------------------------------------- //
	// TRANSIENT FIELDS
	// -------------------------------------------- //
	
	private transient boolean registered = false;
	public boolean isRegistered() { return this.registered; }
	public void register() { this.registered = true; }
	
	private transient Collection<String> desc = new ArrayList<String>();
	public Collection<String> getDesc() { return this.desc; }
	public void setDesc(Collection<String> val) { this.desc = val; }
	public void setDesc(String... val) { this.desc = Arrays.asList(val); }
	
	// -------------------------------------------- //
	// STORED FIELDS
	// -------------------------------------------- //
	
	@SerializedName("mid")
	private String multiverseId;
	public String getMultiverseId() { return this.multiverseId; }
	public void setMultiverseId(String multiverseId) { this.multiverseId = multiverseId; }
	public Multiverse getMultiverse()
	{
		Multiverse ret = MultiverseColl.get().get(this.multiverseId);
		if (ret == null) ret = MultiverseColl.get().get(MassiveCore.DEFAULT);
		return ret;
	}
	public void setMultiverse(Multiverse val) { this.multiverseId = val.getId(); }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public Aspect()
	{
		
	}
	
}
