package com.massivecraft.mcore4.store;

import com.massivecraft.mcore4.store.accessor.Accessor;

/**
 * Usage of this class is highly optional. You may persist anything. If you are 
 * creating the class to be persisted yourself, it might be handy to extend this
 * Entity class. It just contains a set of shortcut methods.  
 */

// Self referencing generic using the "getThis trick".
// http://www.angelikalanger.com/GenericsFAQ/FAQSections/ProgrammingIdioms.html#FAQ206
public abstract class Entity<E extends Entity<E, L>, L>
{
	public abstract Coll<E, L> getColl();
	
	protected abstract E getThis();
	
	public L attach()
	{
		return this.getColl().attach(getThis());
	}
	
	public E detach()
	{
		return this.getColl().detach(getThis());
	}
	
	public boolean attached()
	{
		return this.getColl().getAll().contains(getThis());
	}
	
	public boolean detached()
	{
		return ! this.attached();
	}
	
	public L getId()
	{
		return this.getColl().id(getThis());
	}
	
	// TODO: Perhaps even brute force methods to save or load from remote.
	
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
		return this.getClass().getSimpleName()+this.getColl().mplugin().gson.toJson(this, this.getColl().entityClass());
	}
	
	public E getDefaultInstance()
	{
		return this.getColl().createNewInstance();
	}
	
	public E loadDefaults()
	{
		Accessor.get(this.getColl().entityClass()).copy(this.getDefaultInstance(), this.getThis());
		return this.getThis();
	}
	
	public E load(E entity)
	{
		Accessor.get(this.getColl().entityClass()).copy(entity, this.getThis());
		return this.getThis();
	}
}
