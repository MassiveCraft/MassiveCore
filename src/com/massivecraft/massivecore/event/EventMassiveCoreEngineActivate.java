package com.massivecraft.massivecore.event;

import org.bukkit.event.HandlerList;

import com.massivecraft.massivecore.Engine;

public class EventMassiveCoreEngineActivate extends EventMassiveCoreEngine
{
	// -------------------------------------------- //
	// REQUIRED EVENT CODE
	// -------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	@Override public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public EventMassiveCoreEngineActivate(Engine engine)
	{
		super(engine);
	}
	
}
