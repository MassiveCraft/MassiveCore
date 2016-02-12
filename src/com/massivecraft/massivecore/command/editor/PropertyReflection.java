package com.massivecraft.massivecore.command.editor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

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
		this.setEditable(isEditable(field));
		this.setNullable(isNullable(field));
		this.setName(field.getName());
		this.field = field;
	}
	
	private static boolean isEditable(Field field)
	{
		return ! Modifier.isFinal(field.getModifiers());
	}
	
	private static boolean isNullable(Field field)
	{
		if (field.getType().isPrimitive()) return false;
		
		EditorField setting = field.getAnnotation(EditorField.class);
		if (setting != null && ! setting.nullable()) return false;
		
		return true;
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
	
}
