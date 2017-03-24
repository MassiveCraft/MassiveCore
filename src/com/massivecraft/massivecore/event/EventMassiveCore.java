package com.massivecraft.massivecore.event;

import com.massivecraft.massivecore.collections.MassiveMap;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

import java.util.Map;

public abstract class EventMassiveCore extends Event implements Runnable, Cancellable
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final transient Map<String, Object> customData = new MassiveMap<>();
	public Map<String, Object> getCustomData() { return this.customData; }
	
	private boolean cancelled = false;
	@Override public boolean isCancelled() { return this.cancelled; }
	@Override public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
	
	// -------------------------------------------- //
	// OVERRIDE: RUNNABLE
	// -------------------------------------------- //
	
	@Override
	public void run()
	{
		Bukkit.getPluginManager().callEvent(this);
	}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public EventMassiveCore()
	{
		
	}
	
	public EventMassiveCore(boolean isAsync)
	{
		super(isAsync);
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public boolean isSynchronous()
	{
		return !this.isAsynchronous();
	}
	
}
