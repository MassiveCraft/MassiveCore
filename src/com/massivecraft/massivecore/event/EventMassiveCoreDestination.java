package com.massivecraft.massivecore.event;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.teleport.Destination;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

public class EventMassiveCoreDestination extends EventMassiveCore
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
	
	protected final String arg;
	public String getArg() { return this.arg; }
	
	protected final CommandSender sender;
	public CommandSender getSender() { return this.sender; }
	
	public Destination destination = null;
	public Destination getDestination() { return this.destination; }
	public void setDestination(Destination destination) { this.destination = destination; }
	
	public MassiveException exception = null;
	public MassiveException getException() { return this.exception; }
	public void setException(MassiveException exception) { this.exception = exception; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public EventMassiveCoreDestination(String arg, CommandSender sender, Destination destination)
	{
		this.arg = arg;
		this.sender = sender;
		this.destination = destination;
	}
	
}
