package com.massivecraft.mcore.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

public abstract class MCoreEvent extends Event implements Runnable
{
	// -------------------------------------------- //
	// RUN
	// -------------------------------------------- //
	
	@Override
	public void run()
	{
		Bukkit.getPluginManager().callEvent(this);
	}
	
}
