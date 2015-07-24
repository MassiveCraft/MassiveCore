package com.massivecraft.massivecore;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public interface Engine extends Listener, Runnable
{	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public static final Collection<Engine> ENGINES = new ArrayList<Engine>();
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public Plugin getPlugin();
	
	public void activate();
	public void deactivate();
	
	public Long getDelay();
	public Long getPeriod();
	
	public Integer getTaskId();
	
	public BukkitTask getBukkitTask();
	public boolean isSync();
	
}
