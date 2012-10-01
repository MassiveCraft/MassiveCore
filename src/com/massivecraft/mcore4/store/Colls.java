package com.massivecraft.mcore4.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.massivecraft.mcore4.usys.Aspect;
import com.massivecraft.mcore4.usys.Multiverse;
import com.massivecraft.mcore4.util.MUtil;

public abstract class Colls<C extends Coll<E, L>, E, L>
{
	protected Map<String, C> name2coll = new HashMap<String, C>();
	
	public abstract Aspect getAspect();
	public abstract String getBasename();
	public abstract C createColl(String name);

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public void init()
	{
		this.getColls();
	}
	
	public List<C> getColls()
	{
		List<C> ret = new ArrayList<C>(); 
		Aspect a = this.getAspect();
		Multiverse m = a.multiverse();
		for (String universe : m.getUniverses())
		{
			ret.add(this.getForUniverse(universe));
		}
		return ret;
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public String collnameForUniverse(String universe)
	{
		return this.getBasename() + "@" + universe;
	}
	
	public String universeFromWorldName(String worldName)
	{
		if (worldName == null) throw new IllegalArgumentException("worldName may not be null.");
		
		return this.getAspect().multiverse().getUniverseForWorldName(worldName);
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
			ret.init();
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
