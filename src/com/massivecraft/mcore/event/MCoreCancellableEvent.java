package com.massivecraft.mcore.event;

import org.bukkit.event.Cancellable;

public abstract class MCoreCancellableEvent extends MCoreEvent implements Cancellable
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private boolean cancelled = false;
	@Override public boolean isCancelled() { return this.cancelled; }
	@Override public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
	
}
