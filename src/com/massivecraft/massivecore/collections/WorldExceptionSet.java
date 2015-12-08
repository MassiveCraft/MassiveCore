package com.massivecraft.massivecore.collections;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import com.massivecraft.massivecore.ps.PS;

public class WorldExceptionSet extends ExceptionSet<World>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public WorldExceptionSet()
	{
		
	}
	
	public WorldExceptionSet(boolean standard)
	{
		super(standard);
	}
	
	// -------------------------------------------- //
	// CONTAINS
	// -------------------------------------------- //
	
	@Override
	public String convert(World world)
	{
		return world.getName();
	}
	
	public boolean contains(PS ps)
	{
		return this.contains(ps.getWorld());
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
