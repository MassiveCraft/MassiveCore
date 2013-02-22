package com.massivecraft.mcore.usys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import lombok.Getter;
import lombok.Setter;

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
