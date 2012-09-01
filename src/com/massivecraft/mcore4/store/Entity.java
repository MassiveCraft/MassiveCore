package com.massivecraft.mcore4.store;

import com.massivecraft.mcore4.MCore;
import com.massivecraft.mcore4.store.accessor.Accessor;
import com.massivecraft.mcore4.xlib.gson.Gson;

/**
 * Usage of this class is highly optional. You may persist anything. If you are 
 * creating the class to be persisted yourself, it might be handy to extend this
 * Entity class. It just contains a set of shortcut methods.  
 */

// Self referencing generic using the "getThis trick".
// http://www.angelikalanger.com/GenericsFAQ/FAQSections/ProgrammingIdioms.html#FAQ206
public abstract class Entity<E extends Entity<E, L>, L>
{
	protected transient Coll<E, L> coll;
	protected void setColl(Coll<E, L> val) { this.coll = val; }
	public Coll<E, L> getColl() { return this.coll; }
	
	protected abstract E getThis();
	protected abstract Class<E> getClazz();
	
	public abstract E getDefaultInstance();
	
	public L attach(Coll<E, L> coll)
	{
		return coll.attach(getThis());
	}
	
	public E detach()
	{
		Coll<E, L> coll = this.getColl();
		if (coll == null) return null;
		
		return coll.detach(getThis());
	}
	
	public boolean attached()
	{
		Coll<E, L> coll = this.getColl();
		if (coll == null) return false;
		
		return coll.getAll().contains(getThis());
	}
	
	public boolean detached()
	{
		return ! this.attached();
	}
	
	public L getId()
	{
		Coll<E, L> coll = this.getColl();
		if (coll == null) return null;
		return coll.id(this.getThis());
	}
	
	public void changed()
	{
		L id = this.getId();
		if (id == null) return;
		
		this.getColl().changedIds.add(id);
	}
	
	public void sync()
	{
		L id = this.getId();
		if (id == null) return;
		
		this.getColl().syncId(id);
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
		if (coll != null) gson = coll.mplugin().gson;
		
		return this.getClazz().getSimpleName()+gson.toJson(this, this.getClazz());
	}
	
	public E loadDefaults()
	{
		Accessor.get(this.getClazz()).copy(this.getDefaultInstance(), this.getThis());
		return this.getThis();
	}
	
	public E load(E entity)
	{
		Accessor.get(this.getClazz()).copy(entity, this.getThis());
		return this.getThis();
	}
}
