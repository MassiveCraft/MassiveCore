package com.massivecraft.mcore4.store;

import java.util.HashMap;
import java.util.Map;

import com.massivecraft.mcore4.util.MUtil;

public abstract class Colls<C extends Coll<E, L>, E, L>
{
	protected Map<String, C> name2coll = new HashMap<String, C>();
	
	public abstract C createColl(String name);
	
	public abstract String getContext();
	
	public abstract Db<?> getDb();

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public void init()
	{
		String start = this.collnameStart();
		for (String collname : this.getDb().collnames())
		{
			if ( ! collname.startsWith(start)) continue;
			this.getForCollname(collname);
		}
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public String collnameStart()
	{
		return this.getContext() + "@";
	}
	
	public String universeFromWorldName(String worldName)
	{
		USel selector = USelColl.i.get(this.getContext());
		return selector.select(worldName);
	}
	
	// -------------------------------------------- //
	// GET
	// -------------------------------------------- //
	
	public C getForWorld(String worldName)
	{
		return this.getForUniverse(this.universeFromWorldName(worldName));
	}
	
	public C getForUniverse(String universe)
	{
		String collname = this.collnameStart() + universe;
		return this.getForCollname(collname);
	}
	
	public C getForCollname(String collname)
	{
		C ret = this.name2coll.get(collname);
		if (ret == null)
		{
			ret = this.createColl(collname);
			this.name2coll.put(collname, ret);
		}
		return ret;
	}
	
	public C get(Object worldNameExtractable)
	{
		String worldName = MUtil.extract(String.class, "worldName", worldNameExtractable);
		return this.getForWorld(worldName);
	}
	
	public E get2(Object worldNameExtractable)
	{
		C coll = this.get(worldNameExtractable);
		if (coll == null) return null;
		return coll.get(worldNameExtractable);
	}
}
