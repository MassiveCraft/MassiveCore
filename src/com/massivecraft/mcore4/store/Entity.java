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
	
	// TODO: Mark as dirty.
	// TODO: Perhaps even brute force methods to save or load from remote.
	
	/*public boolean save()
	{
		return this.getColl().saveEntity(getThis());
	}*/
	
	public L getId()
	{
		if (this.getColl() == null) return null;
		return this.getColl().id(getThis());
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
