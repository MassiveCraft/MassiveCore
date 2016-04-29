package com.massivecraft.massivecore.command.editor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.massivecraft.massivecore.command.editor.annotation.EditorEditable;
import com.massivecraft.massivecore.command.editor.annotation.EditorInheritable;
import com.massivecraft.massivecore.command.editor.annotation.EditorNullable;
import com.massivecraft.massivecore.command.editor.annotation.EditorVisible;
import com.massivecraft.massivecore.command.type.RegistryType;
import com.massivecraft.massivecore.command.type.Type;
import com.massivecraft.massivecore.util.ReflectionUtil;

public class PropertyReflection<O, V> extends Property<O, V>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final Field field;
	public Field getField() { return this.field; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public static <O, V> PropertyReflection<O, V> get(Class<O> clazz, String fieldName)
	{
		return get(ReflectionUtil.getField(clazz, fieldName));
	}
	@SuppressWarnings("unchecked")
	public static <O, V> PropertyReflection<O, V> get(Field field)
	{
		Type<O> typeObject = (Type<O>) RegistryType.getType(field.getDeclaringClass());
		Type<V> typeValue = (Type<V>) RegistryType.getType(field);
		return new PropertyReflection<>(typeObject, typeValue, field);
	}
	
	public PropertyReflection(Type<O> typeObject, Type<V> typeValue, Field field)
	{
		super(typeObject, typeValue);
		
		ReflectionUtil.makeAccessible(field);
		this.field = field;
		
		this.setVisible(isVisible(field));
		this.setInheritable(isInheritable(field));
		this.setEditable(isEditable(field));
		this.setNullable(isNullable(field));
		this.setName(field.getName());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public V getRaw(O object)
	{
		return ReflectionUtil.getField(this.getField(), object);
	}
	
	@Override
	public void setRaw(O object, V value)
	{
		ReflectionUtil.setField(this.getField(), object, value);
	}
	
	// -------------------------------------------- //
	// PROPERTY SETTINGS CALCULATION
	// -------------------------------------------- //
	
	public static boolean isVisible(Field field)
	{
		// Create
		boolean ret = true;
		
		// Fill > Standard
		int modifiers = field.getModifiers();
		if (Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers)) ret = false;
		
		// Fill > Annotation
		EditorVisible annotation = field.getAnnotation(EditorVisible.class);
		if (annotation != null) ret = annotation.value();
		
		// Return
		return ret;
	}
	
	public static boolean isInheritable(Field field)
	{
		// Create
		boolean ret = true;
		
		// Fill > Annotation
		EditorInheritable annotation = field.getAnnotation(EditorInheritable.class);
		if (annotation != null) ret = annotation.value();
		
		// Return
		return ret;
	}
	
	public static boolean isEditable(Field field)
	{
		// Create
		boolean ret = true;
		
		// Fill > Standard
		int modifiers = field.getModifiers();
		if (Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers) || Modifier.isFinal(modifiers)) ret = false;
		
		// Fill > Annotation
		EditorEditable annotation = field.getAnnotation(EditorEditable.class);
		if (annotation != null) ret = annotation.value();
		
		// Return
		return ret;
	}
	
	public static boolean isNullable(Field field)
	{
		// Primitive
		if (field.getType().isPrimitive()) return false;
		
		// Create
		boolean ret = true;
		
		// Fill > Annotation
		EditorNullable annotation = field.getAnnotation(EditorNullable.class);
		if (annotation != null) ret = annotation.value();
		
		// Return
		return ret;
	}
	
}
