package com.massivecraft.massivecore.store.accessor;

import java.util.Collection;

public abstract class EntityAccessorAbstract implements EntityAccessor
{
	protected final Class<?> clazz;
	public Class<?> getClazz() { return this.clazz; }
	
	public EntityAccessorAbstract(Class<?> clazz)
	{
		this.clazz = clazz;
	}

	private static final boolean DEFAULT_TRANSPARENT = false;
	@Override
	public void copy(Object from, Object to, String property, boolean transparent)
	{
		Object val = this.get(from, property);
		if (transparent && val == null) return;
		this.set(to, property, val);
	}
	@Override
	public void copy(Object from, Object to, String property)
	{
		this.copy(from, to, property, DEFAULT_TRANSPARENT);
	}
	@Override
	public void copy(Object from, Object to, Collection<String> properties, boolean transparent)
	{
		for (String property : properties)
		{
			this.copy(from, to, property, transparent);
		}
	}
	@Override
	public void copy(Object from, Object to, Collection<String> properties)
	{
		this.copy(from, to, properties, DEFAULT_TRANSPARENT);
	}
	@Override
	public void copy(Object from, Object to, boolean transparent)
	{
		this.copy(from, to, this.getPropertyNames(), transparent);
	}
	@Override
	public void copy(Object from, Object to)
	{
		this.copy(from, to, DEFAULT_TRANSPARENT);
	}
	
}
