package com.massivecraft.mcore;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public interface Engine extends Listener, Runnable
{
	public Plugin getPlugin();
	
	public void activate();
	public void deactivate();
	
	public Long getDelay();
	public Long getPeriod();
	public Integer getTaskId();
}
