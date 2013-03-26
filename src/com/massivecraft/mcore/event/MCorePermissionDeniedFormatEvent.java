package com.massivecraft.mcore.event;

import org.bukkit.event.HandlerList;

public class MCorePermissionDeniedFormatEvent extends MCoreEvent
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
	
	private final String permissionName;
	public String getPermissionName() { return this.permissionName; }
	
	private String format;
	public String getFormat() { return this.format; }
	public void setFormat(String format) { this.format = format; }
	public boolean hasFormat() { return this.format != null; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public MCorePermissionDeniedFormatEvent(String permissionName)
	{
		this.permissionName = permissionName;
		this.format = null;
	}
}