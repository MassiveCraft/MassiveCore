package com.massivecraft.massivecore.event;

import org.bukkit.event.HandlerList;

// "Aknowledge" is in our mind the opposite of "Ignore".
// The purpose of this event is to decide if a unit of communication should be received or ignored.
// A unit of communication can for example be a chat message or a sound effect.
public class EventMassiveCoreAknowledge extends EventMassiveCore
{
	// -------------------------------------------- //
	// REQUIRED EVENT CODE
	// -------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	@Override public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final Object sender;
	public Object getSender() { return this.sender; }
	
	private final Object sendee;
	public Object getSendee() { return this.sendee; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public EventMassiveCoreAknowledge(Object sender, Object sendee)
	{
		if (sender == null) throw new NullPointerException("sender");
		if (sendee == null) throw new NullPointerException("sendee");
		
		this.sender = sender;
		this.sendee = sendee;
	}
	
}
