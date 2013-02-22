package com.massivecraft.mcore.event;

import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

public class MCoreSenderUnregisterEvent extends MCoreSenderEvent
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
	
	public MCoreSenderUnregisterEvent(CommandSender sender)
	{
		super(sender);
	}
	
}
