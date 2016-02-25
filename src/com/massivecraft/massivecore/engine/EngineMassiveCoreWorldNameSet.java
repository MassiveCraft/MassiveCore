package com.massivecraft.massivecore.engine;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import com.massivecraft.massivecore.Engine;

public class EngineMassiveCoreWorldNameSet extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineMassiveCoreWorldNameSet i = new EngineMassiveCoreWorldNameSet();
	public static EngineMassiveCoreWorldNameSet get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void setActiveInner(boolean active)
	{
		if ( ! active) return;
		
		this.worldNamesInner.clear();
		for (World world : Bukkit.getWorlds())
		{
			this.worldNamesInner.add(world.getName());
		}
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final TreeSet<String> worldNamesInner = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
	private final Set<String> worldNamesOuter = Collections.unmodifiableSet(this.worldNamesInner);
	public Set<String> getWorldNames() { return this.worldNamesOuter; }
	
	// -------------------------------------------- //
	// LISTENER
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onWorldLoad(WorldLoadEvent event)
	{
		this.worldNamesInner.add(event.getWorld().getName());
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onWorldUnload(WorldUnloadEvent event)
	{
		this.worldNamesInner.remove(event.getWorld().getName());
	}

}
