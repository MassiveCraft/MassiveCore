package com.massivecraft.massivecore.collections;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import com.massivecraft.massivecore.CaseInsensitiveComparator;
import com.massivecraft.massivecore.ps.PS;

public class WorldExceptionSet
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public boolean standard = true;
	
	public MassiveTreeSet<String, CaseInsensitiveComparator> exceptions = new MassiveTreeSet<String, CaseInsensitiveComparator>(CaseInsensitiveComparator.get());
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public WorldExceptionSet()
	{
		
	}
	
	public WorldExceptionSet(boolean standard)
	{
		this.standard = standard;
	}
	
	// -------------------------------------------- //
	// CONTAINS
	// -------------------------------------------- //
	
	public boolean contains(String world)
	{
		if (this.exceptions.contains(world)) return !this.standard;
		return this.standard;
	}
	
	public boolean contains(PS ps)
	{
		return this.contains(ps.getWorld());
	}
	
	public boolean contains(World world)
	{
		return this.contains(world.getName());
	}
	
	public boolean contains(Location loc)
	{
		return this.contains(loc.getWorld());
	}
	
	public boolean contains(Entity entity)
	{
		return this.contains(entity.getWorld());
	}

}
