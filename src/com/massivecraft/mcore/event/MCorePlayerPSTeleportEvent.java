package com.massivecraft.mcore.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.massivecraft.mcore.ps.PS;

public class MCorePlayerPSTeleportEvent extends Event implements Cancellable, Runnable
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
	
	private boolean cancelled;
	@Override public boolean isCancelled() { return this.cancelled; }
	@Override public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
	
	private final String teleporteeId;
	public String getTeleporteeId() { return this.teleporteeId; }
	
	private final PS from;
	public PS getFrom() { return this.from; }
	
	private PS to;
	public PS getTo() { return this.to; }
	public void setTo(PS to) { this.to = to; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public MCorePlayerPSTeleportEvent(String teleporteeId, PS from, PS to)
	{
		this.teleporteeId = teleporteeId;
		this.from = from;
		this.to = to;
	}
	
	// -------------------------------------------- //
	// RUN
	// -------------------------------------------- //
	
	@Override
	public void run()
	{
		Bukkit.getPluginManager().callEvent(this);
	}
	
}
