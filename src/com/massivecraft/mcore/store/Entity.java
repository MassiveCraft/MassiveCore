package com.massivecraft.mcore.store;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.store.accessor.Accessor;
import com.massivecraft.mcore.xlib.gson.Gson;

/**
 * Usage of this class is highly optional. You may persist anything. If you are 
 * creating the class to be persisted yourself, it might be handy to extend this
 * Entity class. It just contains a set of shortcut methods.  
 */

// Self referencing generic.
// http://www.angelikalanger.com/GenericsFAQ/FAQSections/ProgrammingIdioms.html#FAQ206
public abstract class Entity<E extends Entity<E, L>, L extends Comparable<? super L>> implements Comparable<E>
{
	protected transient Coll<E, L> coll;
	protected void setColl(Coll<E, L> val) { this.coll = val; }
	public Coll<E, L> getColl() { return this.coll; }
	
	protected transient L id;
	protected void setid(L id) { this.id = id; }
	public L getId() { return this.id; }
	
	public String getUniverse()
	{
		Coll<E, L> coll = this.getColl();
		if (coll == null) return null;
		
		return coll.getUniverse();
	}
	
	@SuppressWarnings("unchecked")
	public L attach(Coll<E, L> coll)
	{
		return coll.attach((E) this);
	}
	
	public E detach()
	{
		Coll<E, L> coll = this.getColl();
		if (coll == null) return null;
		
		return coll.detachEntity(this);
	}
	
	public boolean attached()
	{
		return this.getColl() != null && this.getId() != null;
	}
	
	public boolean detached()
	{
		return ! this.attached();
	}
	
	public void changed()
	{
		L id = this.getId();
		if (id == null) return;
		
		this.getColl().changedIds.add(id);
	}
	
	public ModificationState sync()
	{
		L id = this.getId();
		if (id == null) return ModificationState.UNKNOWN;
		return this.getColl().syncId(id);
	}
	
	public void saveToRemote()
	{
		L id = this.getId();
		if (id == null) return;
		
		this.getColl().saveToRemote(id);
	}
	
	public void loadFromRemote()
	{
		L id = this.getId();
		if (id == null) return;
		
		this.getColl().loadFromRemote(id);
	}
	
	@Override
	public String toString()
	{
		Gson gson = MCore.gson;
		Coll<E, L> coll = this.getColl();
		if (coll != null) gson = coll.getMplugin().gson;
		
		return this.getClass().getSimpleName()+gson.toJson(this, this.getClass());
	}
	
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
	
	@Override
	public int compareTo(E that)
	{
		if (that == null) throw new NullPointerException("You cannot compare with null");
		
		if (this.equals(that)) return 0;
		
		L thisId = this.getId();
		L thatId = that.getId();
		
		if (thisId == null) return -1;	
		if (thatId == null) return +1;
		
		int ret = thisId.compareTo(thatId);
		
		// The id's may be the same if these are objects from different collections
		// We avoid zero in an ugly way like this.
		// TODO: Improve by comparing collections and then databases.
		if (ret == 0)
		{
			ret = -1;
		}
		
		return ret;
	}
}
