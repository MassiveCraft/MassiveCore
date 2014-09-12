package com.massivecraft.massivecore;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public interface Engine extends Listener, Runnable
{
	public Plugin getPlugin();
	
	public void activate();
	public void deactivate();
	
	public Long getDelay();
	public Long getPeriod();
	
	public Integer getTaskId();
	
	public BukkitTask getBukkitTask();
	public boolean isSync();
	
}
