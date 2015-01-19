package com.massivecraft.massivecore.event;

import org.bukkit.event.HandlerList;

public class EventMassiveCoreUuidUpdate extends EventMassiveCore
{
	// -------------------------------------------- //
	// REQUIRED EVENT CODE
	// -------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	@Override public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }
	
}
