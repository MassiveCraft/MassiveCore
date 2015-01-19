package com.massivecraft.massivecore;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.plugin.Plugin;

public class MassiveCoreEngineWorldNameSet extends EngineAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MassiveCoreEngineWorldNameSet i = new MassiveCoreEngineWorldNameSet();
	public static MassiveCoreEngineWorldNameSet get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Plugin getPlugin()
	{
		return MassiveCore.get();
	}
	
	@Override
	public void activate()
	{
		super.activate();
		
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
