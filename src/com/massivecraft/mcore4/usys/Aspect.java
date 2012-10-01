package com.massivecraft.mcore4.usys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import lombok.Getter;
import lombok.Setter;

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
	
	@Getter protected transient boolean registered = false;
	public void register() { this.registered = true; }
	
	@Getter protected transient Collection<String> desc = new ArrayList<String>();
	public void setDesc(Collection<String> val) { this.desc = val; }
	public void setDesc(String... val) { this.desc = Arrays.asList(val); }
	
	// -------------------------------------------- //
	// STORED FIELDS
	// -------------------------------------------- //
	
	@SerializedName("mid")
	@Getter @Setter protected String multiverseId;
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
