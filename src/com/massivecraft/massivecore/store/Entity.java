package com.massivecraft.massivecore.store;

import com.massivecraft.massivecore.xlib.gson.JsonObject;

/**
 * Usage of this class is highly optional. You may persist anything. If you are 
 * creating the class to be persisted yourself, it might be handy to extend this
 * Entity class. It just contains a set of shortcut methods.  
 */

// Self referencing generic.
// http://www.angelikalanger.com/GenericsFAQ/FAQSections/ProgrammingIdioms.html#FAQ206
public class Entity<E extends Entity<E>> extends EntityInternal<E>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public Coll<?> getColl() { return (Coll<?>) this.getContainer(); }
	
	public String getUniverse()
	{
		Coll<?> coll = this.getColl();
		if (coll == null) return null;
		
		return coll.getUniverse();
	}
	
	private volatile transient JsonObject lastRaw = null;
	public JsonObject getLastRaw() { return this.lastRaw; }
	public void setLastRaw(JsonObject lastRaw) { this.lastRaw = lastRaw; }
	
	private volatile transient long lastMtime = 0;
	public long getLastMtime() { return this.lastMtime; }
	public void setLastMtime(long lastMtime) { this.lastMtime = lastMtime; }
	
	private volatile transient boolean lastDefault = false;
	public boolean getLastDefault() { return this.lastDefault; }
	public void setLastDefault(boolean lastDefault) { this.lastDefault = lastDefault; }
	
	public void clearSyncLogFields()
	{
		this.lastRaw = null;
		this.lastMtime = 0;
		this.lastDefault = false;
	}
	
	// -------------------------------------------- //
	// ATTACH AND DETACH
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	public String attach(EntityContainer<E> container)
	{
		if (!(container instanceof Coll)) throw new IllegalArgumentException(container.getClass().getName() + " is not a Coll.");
		return container.attach((E) this);
	}
	
	@SuppressWarnings("unchecked")
	public E detach()
	{
		EntityContainer<E> coll = this.getContainer();
		if (coll == null) return (E)this;
		
		return coll.detachEntity((E) this);
	}

	// -------------------------------------------- //
	// SYNC AND IO ACTIONS
	// -------------------------------------------- //
	
	public Modification sync()
	{
		if (!this.isLive()) return Modification.UNKNOWN;
		return this.getColl().syncIdFixed(id);
	}
	
	public void saveToRemote()
	{
		if (!this.isLive()) return;
		
		this.getColl().saveToRemoteFixed(id);
	}
	
	public void loadFromRemote()
	{
		if (!this.isLive()) return;
		
		this.getColl().loadFromRemoteFixed(id, null);
	}
	
}
