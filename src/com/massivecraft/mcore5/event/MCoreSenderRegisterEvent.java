package com.massivecraft.mcore5.event;

import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

public class MCoreSenderRegisterEvent extends MCoreSenderEvent
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
	
	public MCoreSenderRegisterEvent(CommandSender sender)
	{
		super(sender);
	}
	
}
