package com.massivecraft.massivecore.store;

import java.util.Objects;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.accessor.Accessor;
import com.massivecraft.massivecore.xlib.gson.Gson;
import com.massivecraft.massivecore.xlib.gson.JsonObject;

/**
 * Usage of this class is highly optional. You may persist anything. If you are 
 * creating the class to be persisted yourself, it might be handy to extend this
 * Entity class. It just contains a set of shortcut methods.  
 */

// Self referencing generic.
// http://www.angelikalanger.com/GenericsFAQ/FAQSections/ProgrammingIdioms.html#FAQ206
public class Entity<E extends Entity<E>>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected transient Coll<E> coll;
	protected void setColl(Coll<E> val) { this.coll = val; }
	public Coll<E> getColl() { return this.coll; }
	
	protected transient String id;
	protected void setId(String id) { this.id = id; }
	public String getId() { return this.id; }
	
	public String getUniverse()
	{
		Coll<E> coll = this.getColl();
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
	public String attach(Coll<E> coll)
	{
		return coll.attach((E) this);
	}
	
	@SuppressWarnings("unchecked")
	public E detach()
	{
		Coll<E> coll = this.getColl();
		if (coll == null) return (E)this;
		
		return coll.detachEntity((E) this);
	}
	
	public boolean attached()
	{
		return this.getColl() != null && this.getId() != null;
	}
	
	public boolean detached()
	{
		return ! this.attached();
	}
	
	
	public void preAttach(String id)
	{
		
	}
	
	public void postAttach(String id)
	{
		
	}
	
	public void preDetach(String id)
	{
		
	}
	
	public void postDetach(String id)
	{
		
	}
	
	// -------------------------------------------- //
	// SYNC AND IO ACTIONS
	// -------------------------------------------- //
	
	public boolean isLive()
	{
		String id = this.getId();
		if (id == null) return false;
		
		Coll<E> coll = this.getColl();
		if (coll == null) return false;
		
		if ( ! coll.inited()) return false;
		
		return true;
	}
	
	public void changed()
	{
		if ( ! this.isLive()) return;
		
		//System.out.println(this.getColl().getName() + ": " +this.getId() + " was modified locally");
		
		// UNKNOWN is very unimportant really.
		// LOCAL_ATTACH is for example much more important and should not be replaced.
		this.getColl().putIdentifiedModificationFixed(this.getId(), Modification.UNKNOWN);
	}
	
	public Modification sync()
	{
		if ( ! this.isLive()) return Modification.UNKNOWN;
		return this.getColl().syncIdFixed(id);
	}
	
	public void saveToRemote()
	{
		if ( ! this.isLive()) return;
		
		this.getColl().saveToRemoteFixed(id);
	}
	
	public void loadFromRemote()
	{
		if ( ! this.isLive()) return;
		
		this.getColl().loadFromRemoteFixed(id, null);
	}
	
	// -------------------------------------------- //
	// DERPINGTON
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	public E load(E that)
	{
		Accessor.get(this.getClass()).copy(that, this);
		return (E) this;
	}
	
	public boolean isDefault()
	{
		return false;
	}
	
	// -------------------------------------------- //
	// CONVENIENCE: DATABASE
	// -------------------------------------------- //

	// GENERIC
	public <T> T convertGet(T value, T defaultValue)
	{
		return value != null ? value : defaultValue;
	}
	
	public <T> T convertSet(T value, T defaultValue)
	{
		this.changed();
		return Objects.equals(value, defaultValue) ? null : value;
	}
	
	// BOOLEAN
	public boolean convertGet(Boolean value)
	{
		return convertGet(value, false);
	}
	
	public Boolean convertSet(Boolean value)
	{
		return convertSet(value, false);
	}
	
	// -------------------------------------------- //
	// STANDARDS
	// -------------------------------------------- //

	@Override
	public String toString()
	{
		Gson gson = MassiveCore.gson;
		Coll<E> coll = this.getColl();
		if (coll != null) gson = coll.getGson();
		
		return this.getClass().getSimpleName()+gson.toJson(this, this.getClass());
	}
	
}
