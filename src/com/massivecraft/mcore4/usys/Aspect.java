package com.massivecraft.mcore4.usys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.massivecraft.mcore4.MCore;
import com.massivecraft.mcore4.store.Entity;
import com.massivecraft.mcore4.xlib.gson.annotations.SerializedName;

public class Aspect extends Entity<Aspect, String>
{	
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	@Override protected Aspect getThis() { return this; }
	
	private final static transient Aspect defaultInstance = new Aspect();
	@Override public Aspect getDefaultInstance(){ return defaultInstance; }
	@Override protected Class<Aspect> getClazz() { return Aspect.class; }
	
	public static Aspect get(Object oid)
	{
		return AspectColl.i.get(oid);
	}
	
	// -------------------------------------------- //
	// TRANSIENT FIELDS
	// -------------------------------------------- //
	
	protected transient boolean registered = false;
	public boolean registered() { return this.registered; }
	public void register() { this.registered = true; }
	
	protected transient Collection<String> desc = new ArrayList<String>();
	public Collection<String> desc() { return this.desc; }
	public void desc(Collection<String> val) { this.desc = val; }
	public void desc(String... val) { this.desc = Arrays.asList(val); }
	
	// -------------------------------------------- //
	// STORED FIELDS
	// -------------------------------------------- //
	
	@SerializedName("mid")
	protected String multiverseId;
	public String multiverseId() { return this.multiverseId; }
	public void multiverseId(String val) { this.multiverseId = val; }
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
