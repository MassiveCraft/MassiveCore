package com.massivecraft.mcore.event;

import org.bukkit.event.HandlerList;

public class MCoreUuidUpdateEvent extends MCoreEvent
{
	// -------------------------------------------- //
	// REQUIRED EVENT CODE
	// -------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	@Override public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }
	
}
