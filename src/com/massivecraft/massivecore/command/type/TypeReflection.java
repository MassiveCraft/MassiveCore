package com.massivecraft.massivecore.command.type;

import com.massivecraft.massivecore.command.editor.annotation.EditorName;

public class TypeReflection<T> extends TypeAbstractChoice<T> 
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static <T> TypeReflection<T> get(Class<T> clazz) { return new TypeReflection<>(clazz); }
	public TypeReflection(Class<T> clazz)
	{
		super(clazz);
		this.setInnerProperties(clazz);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getName()
	{
		return this.getClazz().getSimpleName();
	}	

	@Override
	public String getIdInner(T value)
	{
		return getId(value.getClass());
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static String getId(Class<?> clazz)
	{
		EditorName ann = clazz.getAnnotation(EditorName.class);
		if (ann != null) return ann.value();
		
		return clazz.getSimpleName();
	}
	
}
