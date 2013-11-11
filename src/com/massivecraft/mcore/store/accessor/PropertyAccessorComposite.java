package com.massivecraft.mcore.store.accessor;

public class PropertyAccessorComposite implements PropertyAccessor
{
	private final PropertyGetter getter;
	private final PropertySetter setter;
	
	public PropertyAccessorComposite(PropertyGetter getter, PropertySetter setter)
	{
		this.getter = getter;
		this.setter = setter;
	}

	@Override
	public void set(Object entity, Object val)
	{
		this.setter.set(entity, val);
	}

	@Override
	public Object get(Object entity)
	{
		return this.getter.get(entity);
	}
	
}
