package com.massivecraft.mcore5.usys;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.massivecraft.mcore5.MCore;
import com.massivecraft.mcore5.cmd.arg.ARUniverse;
import com.massivecraft.mcore5.store.Entity;
import com.massivecraft.mcore5.util.MUtil;

public class Multiverse extends Entity<Multiverse, String>
{	
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	@Override protected Multiverse getThis() { return this; }
	
	private final static transient Multiverse defaultInstance = new Multiverse();
	@Override public Multiverse getDefaultInstance(){ return defaultInstance; }
	@Override protected Class<Multiverse> getClazz() { return Multiverse.class; }
	
	public static Multiverse get(Object oid)
	{
		return MultiverseColl.i.get(oid);
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected Map<String, Set<String>> uw = new HashMap<String, Set<String>>();
	
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public Multiverse()
	{
		
	}
	
	// -------------------------------------------- //
	// ASPECTS
	// -------------------------------------------- //
	
	public List<Aspect> myAspects()
	{
		return AspectColl.i.getAllRegisteredForMultiverse(this, true);
	}
	
	public List<Aspect> otherAspects()
	{
		return AspectColl.i.getAllRegisteredForMultiverse(this, false);
	}
	
	// -------------------------------------------- //
	// UNIVERSE
	// -------------------------------------------- //
	
	public boolean containsUniverse(String worldName)
	{
		return this.getUniverses().contains(worldName);
	}
	
	public Set<String> newUniverse(String universe)
	{
		if (universe.equals(MCore.DEFAULT)) return null;
		Set<String> ret = this.uw.get(universe);
		if (ret == null)
		{
			ret = new HashSet<String>();
			this.uw.put(universe, ret);
		}
		return ret;
	}
	
	public Set<String> delUniverse(String universe)
	{
		return this.uw.remove(universe);
	}
	
	public Set<String> getUniverses()
	{
		Set<String> ret = new LinkedHashSet<String>();
		ret.addAll(this.uw.keySet());
		ret.add(MCore.DEFAULT);
		return ret;
	}
	
	public String getUniverseForWorldName(String worldName)
	{
		for (Entry<String, Set<String>> entry : this.uw.entrySet())
		{
			String universe = entry.getKey();
			Set<String> worlds = entry.getValue();
			if (worlds.contains(worldName)) return universe;
		}
		return MCore.DEFAULT;
	}
	
	public String getUniverse(Object worldNameExtractable)
	{
		String worldName = MUtil.extract(String.class, "worldName", worldNameExtractable);
		return this.getUniverseForWorldName(worldName);
	}
	
	// -------------------------------------------- //
	// UNIVERSE AND WORLD
	// -------------------------------------------- //
	
	public boolean clearUniverse(String universe)
	{
		Set<String> worlds = this.uw.get(universe);
		if (worlds == null) return false;
		worlds.clear();
		return true;
	}
	
	public boolean setWorldUniverse(String worldName, String universe)
	{
		if (this.getUniverseForWorldName(worldName).equals(universe)) return false;
		this.removeWorld(worldName);
		if (!universe.equals(MCore.DEFAULT))
		{
			this.newUniverse(universe).add(worldName);
		}
		return true;
	}
	
	// -------------------------------------------- //
	// WORLD
	// -------------------------------------------- //
	
	public boolean containsWorld(String worldName)
	{
		return this.getWorlds().contains(worldName);
	}
	
	public Set<String> getWorlds()
	{
		Set<String>ret = new HashSet<String>();
		for (Set<String> uworlds : this.uw.values())
		{
			ret.addAll(uworlds);
		}
		return ret;
	}
	
	public Set<String> getWorlds(String universe)
	{
		return this.uw.get(universe);
	}
	
	public boolean removeWorld(String worldName)
	{
		for (Set<String> worldNames : this.uw.values())
		{
			if(worldNames.remove(worldName)) return true;
		}
		return false;
	}
	
	// -------------------------------------------- //
	// ARG READERS
	// -------------------------------------------- //
	
	public ARUniverse argReaderUniverse()
	{
		return new ARUniverse(this);
	}
	
}
