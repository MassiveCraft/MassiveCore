package com.massivecraft.massivecore.store.accessor;

import com.massivecraft.massivecore.store.EntityInternalMap;

import java.lang.reflect.Field;

public class FieldAccessorInternalEntityMap extends FieldAccessorSimple
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public FieldAccessorInternalEntityMap(Field field)
	{
		super(field);
	}
	
	// -------------------------------------------- //
	// CORE
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	public void set(Object entity, Object val)
	{
		EntityInternalMap entityMap = (EntityInternalMap) this.get(entity);
		EntityInternalMap that = (EntityInternalMap) val;
		
		entityMap.load(that);
	}

}
