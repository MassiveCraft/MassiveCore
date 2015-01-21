package com.massivecraft.massivecore;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import com.massivecraft.massivecore.cmd.arg.ARUniverse;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;

public class Multiverse extends Entity<Multiverse>
{	
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	public static Multiverse get(Object oid)
	{
		return MultiverseColl.get().get(oid);
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
		return AspectColl.get().getAllRegisteredForMultiverse(this, true);
	}
	
	public List<Aspect> otherAspects()
	{
		return AspectColl.get().getAllRegisteredForMultiverse(this, false);
	}
	
	// -------------------------------------------- //
	// UNIVERSE
	// -------------------------------------------- //
	
	public boolean containsUniverse(String universe)
	{
		return this.getUniverses().contains(universe);
	}
	
	public Set<String> newUniverse(String universe)
	{
		if (universe.equals(MassiveCore.DEFAULT)) return null;
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
		Set<String> ret = new TreeSet<String>();
		ret.addAll(this.uw.keySet());
		ret.add(MassiveCore.DEFAULT);
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
		return MassiveCore.DEFAULT;
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
		if (!universe.equals(MassiveCore.DEFAULT))
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
		Set<String> ret = new TreeSet<String>();
		for (Set<String> uworlds : this.uw.values())
		{
			ret.addAll(uworlds);
		}
		return ret;
	}
	
	public Set<String> getWorlds(String universe)
	{
		Set<String> orig = this.uw.get(universe);
		if (orig == null) return null;
		
		Set<String> ret = new TreeSet<String>();
		ret.addAll(orig);
		
		return ret;
	}
	
	public boolean removeWorld(String worldName)
	{
		for (Set<String> worldNames : this.uw.values())
		{
			if (worldNames.remove(worldName)) return true;
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
