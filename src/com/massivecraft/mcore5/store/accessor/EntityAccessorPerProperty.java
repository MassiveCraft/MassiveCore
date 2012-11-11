package com.massivecraft.mcore5.store.accessor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EntityAccessorPerProperty extends EntityAccessorAbstract
{
	protected Map<String, PropertyAccessor> propertyAccessors;
	public Map<String, PropertyAccessor> getPropertyAccessors() {	return this.propertyAccessors; }
	public PropertyAccessor getPropertyAccessor(String name)
	{
		PropertyAccessor ret = this.propertyAccessors.get(name);
		if (ret == null)
		{
			throw new IllegalArgumentException("The property \""+name+"\" is not supported.");	
		}
		return ret;
	}
	public void setPropertyAccessor(String name, PropertyAccessor val)
	{
		this.propertyAccessors.put(name, val);
	}
	
	
	//----------------------------------------------//
	// CONSTRUCTORS
	//----------------------------------------------//
	
	public EntityAccessorPerProperty(Class<?> clazz)
	{
		super(clazz);
		this.propertyAccessors = new HashMap<String, PropertyAccessor>();
		this.populateAI1();
	}
	
	//----------------------------------------------//
	// AI
	//----------------------------------------------//
	
	public void populateAI1()
	{
		this.propertyAccessorAI(AccessorUtil.getFieldMap(this.clazz).keySet());
	}
	
	public void propertyAccessorAI(String name)
	{
		this.propertyAccessors.put(name, AccessorUtil.createPropertyAccessor(this.clazz, name));
	}
	public void propertyAccessorAI(String... names)
	{
		for (String name : names)
		{
			this.propertyAccessorAI(name);
		}
	}
	public void propertyAccessorAI(Collection<? extends String> names)
	{
		this.propertyAccessorAI(names.toArray(new String[0]));
	}
	
	//----------------------------------------------//
	// IMPLEMENTATION
	//----------------------------------------------//

	@Override
	public void set(Object entity, String property, Object val)
	{
		PropertyAccessor pa = this.getPropertyAccessor(property);
		pa.set(entity, val);
	}

	@Override
	public Object get(Object entity, String property)
	{
		PropertyAccessor pa = this.getPropertyAccessor(property);
		return pa.get(entity);
	}
	
	@Override
	public Collection<String> getPropertyNames()
	{
		return this.propertyAccessors.keySet();
	}
}
