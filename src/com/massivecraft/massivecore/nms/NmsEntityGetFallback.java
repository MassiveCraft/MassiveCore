package com.massivecraft.massivecore.nms;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class NmsEntityGetFallback extends NmsEntityGet
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsEntityGetFallback i = new NmsEntityGetFallback();
	public static NmsEntityGetFallback get () { return i; }
	
	// -------------------------------------------- //
	// GET ENTITY
	// -------------------------------------------- //
	
	@Override
	public Entity getEntity(UUID uuid)
	{
		if (uuid == null) return null;
		
		for (World world : Bukkit.getWorlds())
		{
			Entity ret = this.getEntity(world, uuid);
			if (ret != null) return ret;
		}
		
		return null;
	}
	
	@Override
	public Entity getEntity(World world, UUID uuid)
	{
		if (world == null) throw new NullPointerException("world");
		if (uuid == null) return null;
		
		for (Entity entity : world.getEntities())
		{
			if (entity.getUniqueId().equals(uuid)) return entity;
		}
		
		return null;
	}
	
}
