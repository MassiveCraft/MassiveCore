package com.massivecraft.massivecore.event;

import com.massivecraft.massivecore.Log;
import org.bukkit.event.HandlerList;

public class EventMassiveCoreLog extends EventMassiveCore
{
	// -------------------------------------------- //
	// REQUIRED EVENT CODE
	// -------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	@Override public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }
	
	// -------------------------------------------- //
	// FIELD
	// -------------------------------------------- //
	
	private final Log log;
	public Log getLog() { return this.log; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public EventMassiveCoreLog(Log log)
	{
		this.log = log;
	}
	
}
