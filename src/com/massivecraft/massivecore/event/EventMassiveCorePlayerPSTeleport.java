package com.massivecraft.massivecore.event;

import org.bukkit.event.HandlerList;

import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.teleport.Destination;

public class EventMassiveCorePlayerPSTeleport extends EventMassiveCore
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
	
	protected final String teleporteeId;
	public String getTeleporteeId() { return this.teleporteeId; }
	
	protected final PS origin;
	public PS getOrigin() { return this.origin; }
	
	protected Destination destination;
	public Destination getDestination() { return this.destination; }
	public void setDestination(Destination destination) { this.destination = destination; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public EventMassiveCorePlayerPSTeleport(String teleporteeId, PS origin, Destination destination)
	{
		this.teleporteeId = teleporteeId;
		this.origin = origin;
		this.destination = destination;
	}
	
}
