package com.massivecraft.massivecore.store.accessor;

public interface FieldAccessor
{
	public Object get(Object entity);
	public void set(Object entity, Object val);

}
