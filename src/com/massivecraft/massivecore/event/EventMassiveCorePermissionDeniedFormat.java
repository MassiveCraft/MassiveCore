package com.massivecraft.massivecore.event;

import org.bukkit.event.HandlerList;

public class EventMassiveCorePermissionDeniedFormat extends EventMassiveCore
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
	
	private final String permissionId;
	public String getPermissionId() { return this.permissionId; }
	
	private String format;
	public String getFormat() { return this.format; }
	public void setFormat(String format) { this.format = format; }
	public boolean hasFormat() { return this.format != null; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public EventMassiveCorePermissionDeniedFormat(String permissionId)
	{
		this.permissionId = permissionId;
		this.format = null;
	}
	
}
