package com.massivecraft.massivecore.item;

import java.lang.reflect.Field;

import com.massivecraft.massivecore.util.ReflectionUtil;

public abstract class WriterAbstractReflect<OA, OB, CA, CB, FA, FB> extends WriterAbstract<OA, OB, CA, CB, FA, FB>
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
		this.field = (fieldName == null ? null : ReflectionUtil.getField(clazz, fieldName)); 
	}
	
	// -------------------------------------------- //
	// ACCESS
	// -------------------------------------------- //
	
	@Override
	public void setB(CB cb, FB fb)
	{
		if (this.field == null) return;
		ReflectionUtil.setField(this.field, cb, fb);
	}
	
}
