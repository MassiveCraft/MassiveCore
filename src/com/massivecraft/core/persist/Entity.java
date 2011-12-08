package com.massivecraft.core.persist;

/**
 * Usage of this class is highly optional. You may persist anything. If you are 
 * creating the class to be persisted yourself, it might be handy to extend this
 * Entity class. It just contains a set of shortcut methods.  
 */

// Self referencing generic using the "getThis trick".
// http://www.angelikalanger.com/GenericsFAQ/FAQSections/ProgrammingIdioms.html#FAQ206
public abstract class Entity<T extends Entity<T>>
{
	public abstract IClassManager<T> getManager();
	protected abstract T getThis();
	
	public String attach()
	{
		return this.getManager().attach(getThis());
	}
	
	public void detach()
	{
		this.getManager().detachEntity(getThis());
	}
	
	public boolean attached()
	{
		return this.getManager().containsEntity(getThis());
	}
	
	public boolean detached()
	{
		return ! this.attached();
	}
	
	public boolean save()
	{
		return this.getManager().saveEntity(getThis());
	}
	
	public String getId()
	{
		return this.getManager().id(getThis());
	}
}
