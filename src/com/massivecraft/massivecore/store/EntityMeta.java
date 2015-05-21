package com.massivecraft.massivecore.store;

import com.massivecraft.massivecore.xlib.gson.JsonElement;

//TODO: Merge with entity
public class EntityMeta
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final JsonElement DEFAULT_LAST_RAW = null;
	public static final long DEFAULT_MTIME = 0;
	public static final boolean DEFAULT_DEFAULT = false;
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	private volatile JsonElement lastRaw = DEFAULT_LAST_RAW;
	public JsonElement getLastRaw() { return this.lastRaw; }
	public void setLastRaw(JsonElement lastRaw) { this.lastRaw = lastRaw; }
	
	private volatile long mtime = DEFAULT_MTIME;
	public long getMtime() { return this.mtime; }
	public void setMtime(long mtime) { this.mtime = mtime; }
	
	private volatile boolean isDefault = DEFAULT_DEFAULT;
	public boolean isDefault() { return this.isDefault; }
	public void setDefault(boolean isDefault) { this.isDefault = isDefault; }

}
