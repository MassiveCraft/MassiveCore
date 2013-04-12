package com.massivecraft.mcore.store;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.NaturalOrderComparator;
import com.massivecraft.mcore.store.accessor.Accessor;
import com.massivecraft.mcore.xlib.gson.Gson;

/**
 * Usage of this class is highly optional. You may persist anything. If you are 
 * creating the class to be persisted yourself, it might be handy to extend this
 * Entity class. It just contains a set of shortcut methods.  
 */

// Self referencing generic.
// http://www.angelikalanger.com/GenericsFAQ/FAQSections/ProgrammingIdioms.html#FAQ206
public abstract class Entity<E extends Entity<E>> implements Comparable<E>
{
	protected transient Coll<E> coll;
	protected void setColl(Coll<E> val) { this.coll = val; }
	public Coll<E> getColl() { return this.coll; }
	
	protected transient String id;
	protected void setid(String id) { this.id = id; }
	public String getId() { return this.id; }
	
	public String getUniverse()
	{
		Coll<E> coll = this.getColl();
		if (coll == null) return null;
		
		return coll.getUniverse();
	}
	
	@SuppressWarnings("unchecked")
	public String attach(Coll<E> coll)
	{
		return coll.attach((E) this);
	}
	
	public E detach()
	{
		Coll<E> coll = this.getColl();
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
		String id = this.getId();
		if (id == null) return;
		
		this.getColl().changedIds.add(id);
	}
	
	public ModificationState sync()
	{
		String id = this.getId();
		if (id == null) return ModificationState.UNKNOWN;
		return this.getColl().syncId(id);
	}
	
	public void saveToRemote()
	{
		String id = this.getId();
		if (id == null) return;
		
		this.getColl().saveToRemote(id);
	}
	
	public void loadFromRemote()
	{
		String id = this.getId();
		if (id == null) return;
		
		this.getColl().loadFromRemote(id);
	}
	
	@Override
	public String toString()
	{
		Gson gson = MCore.gson;
		Coll<E> coll = this.getColl();
		if (coll != null) gson = coll.getGson();
		
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
		
		String thisId = this.getId();
		String thatId = that.getId();
		
		if (thisId == null) return -1;	
		if (thatId == null) return +1;
		
		int ret = NaturalOrderComparator.get().compare(thisId, thatId);
		
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
