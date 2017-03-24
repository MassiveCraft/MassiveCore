package com.massivecraft.massivecore.item;

import com.massivecraft.massivecore.util.ReflectionUtil;

import java.lang.reflect.Field;

public abstract class WriterAbstractReflect<OA, OB, CA, CB, FA, FB> extends WriterAbstract<OA, OB, CA, CB, FA, FB, Object>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final Field field;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public WriterAbstractReflect(Class<?> clazz, String fieldName)
	{
		super(null, null);
		this.field = (fieldName == null ? null : ReflectionUtil.getField(clazz, fieldName)); 
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //
	
	@Override
	public void setB(CB cb, FB fb, Object d)
	{
		if (this.field == null) return;
		ReflectionUtil.setField(this.field, cb, fb);
	}
	
}
