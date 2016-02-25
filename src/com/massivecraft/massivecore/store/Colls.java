package com.massivecraft.massivecore.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.massivecraft.massivecore.Active;
import com.massivecraft.massivecore.Aspect;
import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.Multiverse;
import com.massivecraft.massivecore.util.MUtil;

public abstract class Colls<C extends Coll<E>, E extends Entity<E>> implements Active
{
	protected Map<String, C> name2coll = new HashMap<String, C>();
	
	public abstract Aspect getAspect();
	public abstract String getBasename();
	public abstract C createColl(String name);

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public List<C> getColls()
	{
		List<C> ret = new ArrayList<C>(); 
		Aspect a = this.getAspect();
		Multiverse m = a.getMultiverse();
		for (String universe : m.getUniverses())
		{
			ret.add(this.getForUniverse(universe));
		}
		return ret;
	}
	
	// -------------------------------------------- //
	// ACTIVE
	// -------------------------------------------- //
	
	private boolean active = false; 
	@Override
	public boolean isActive()
	{
		return this.active;
	}
	@Override
	public void setActive(boolean active)
	{
		this.active = active;
		if (active)
		{
			this.getColls();
		}
		else
		{
			// TODO: Uuuuuh
		}
	}
	
	private MassivePlugin plugin = null;
	@Override
	public MassivePlugin setActivePlugin(MassivePlugin plugin)
	{
		MassivePlugin ret = this.plugin;
		this.plugin = plugin;
		return ret;
	}
	@Override
	public MassivePlugin getActivePlugin()
	{
		return this.plugin;
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
		
		return this.getAspect().getMultiverse().getUniverseForWorldName(worldName);
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
			ret.setActivePlugin(this.getActivePlugin());
			ret.setActive(true);
		}
		return ret;
	}
	
	public C get(Object worldNameExtractable)
	{
		if (worldNameExtractable == null) return null;
		String worldName = MUtil.extract(String.class, "worldName", worldNameExtractable);
		if (worldName == null) return null;
		return this.getForWorld(worldName);
	}
	
	public E get2(Object worldNameExtractable)
	{
		C coll = this.get(worldNameExtractable);
		if (coll == null) return null;
		return coll.get(worldNameExtractable);
	}
}
