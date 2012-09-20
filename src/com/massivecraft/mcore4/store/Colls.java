package com.massivecraft.mcore4.store;

import java.util.HashMap;
import java.util.Map;

import com.massivecraft.mcore4.usys.Aspect;
import com.massivecraft.mcore4.util.MUtil;

public abstract class Colls<C extends Coll<E, L>, E, L>
{
	protected Map<String, C> name2coll = new HashMap<String, C>();
	public Map<String, C> name2coll() { return this.name2coll; }
	
	public abstract C createColl(String name);
	
	public abstract Aspect aspect();
	public abstract String basename();
	
	public abstract Db<?> getDb();

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public void init()
	{
		String start = this.collnameForUniverse("");
		for (String collname : this.getDb().collnames())
		{
			if ( ! collname.startsWith(start)) continue;
			this.getForCollname(collname);
		}
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public String collnameForUniverse(String universe)
	{
		return this.basename() + "@" + universe;
	}
	
	public String universeFromWorldName(String worldName)
	{
		if (worldName == null) throw new IllegalArgumentException("worldName may not be null.");
		
		return this.aspect().multiverse().getUniverseForWorldName(worldName);
	}
	
	// -------------------------------------------- //
	// GET
	// -------------------------------------------- //
	
	public C getForWorld(String worldName)
	{
		if (worldName == null) throw new IllegalArgumentException("worldName may not be null.");
		
		return this.getForUniverse(this.universeFromWorldName(worldName));
	}
	
	public C getForUniverse(String universe)
	{
		if (universe == null) throw new IllegalArgumentException("universe may not be null.");
		String collname = this.collnameForUniverse(universe);
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
