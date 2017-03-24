package com.massivecraft.massivecore.command.editor;

import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.editor.annotation.EditorEditable;
import com.massivecraft.massivecore.command.editor.annotation.EditorInheritable;
import com.massivecraft.massivecore.command.editor.annotation.EditorMethods;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.command.editor.annotation.EditorNullable;
import com.massivecraft.massivecore.command.editor.annotation.EditorVisible;
import com.massivecraft.massivecore.command.type.RegistryType;
import com.massivecraft.massivecore.command.type.Type;
import com.massivecraft.massivecore.command.type.TypeWrapper;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.particleeffect.ReflectionUtils;
import com.massivecraft.massivecore.store.migrator.MigratorUtil;
import com.massivecraft.massivecore.util.ReflectionUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

public class PropertyReflection<O, V> extends Property<O, V>
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final boolean VISIBLE_DEFAULT = true;
	public static final boolean INHERITABLE_DEFAULT = true;
	public static final boolean EDITABLE_DEFAULT = true;
	public static final boolean NULLABLE_DEFAULT = false;
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final Field field;
	public Field getField() { return this.field; }
	
	private final Method getter;
	public Method getGetter() { return this.getter; }
	
	private final Method setter;
	public Method getSetter() { return this.setter; }
	
	public static Method calcGetter(Field field)
	{
		String name = Txt.upperCaseFirst(field.getName());
		
		if ( ! getAnnotationValue(field, EditorMethods.class, false)) return null;
		
		// Try with normal get
		if (ReflectionUtil.hasMethod(field.getDeclaringClass(), "get" + name))
		{
			return ReflectionUtil.getMethod(field.getDeclaringClass(), "get" + name);
		}
		
		// For booleans try other options
		if ( ! (boolean.class.equals(field.getType()) || Boolean.class.equals(field.getType()))) throw new RuntimeException(field.toString());
		
		// Try is
		if (ReflectionUtil.hasMethod(field.getDeclaringClass(), "is" + name))
		{
			return ReflectionUtil.getMethod(field.getDeclaringClass(), "is" + name);
		}
		
		// Try has
		if (ReflectionUtil.hasMethod(field.getDeclaringClass(), "has" + name))
		{
			return ReflectionUtil.getMethod(field.getDeclaringClass(), "has" + name);
		}
		
		// Fail
		throw new RuntimeException(field.toString());
	}
	public static Method calcSetter(Field field)
	{
		String name = Txt.upperCaseFirst(field.getName());
		
		if ( ! getAnnotationValue(field, EditorMethods.class, false)) return null;
		
		try
		{
			return ReflectionUtils.getMethod(field.getDeclaringClass(), "set" + name, field.getType());
		}
		catch (NoSuchMethodException e)
		{
			// Fail
			throw new RuntimeException(field.toString());
		}
	}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public static <O> List<PropertyReflection<O, ?>> getAll(Class<O> clazz, Type<O> typeObject)
	{
		List<PropertyReflection<O, ?>> ret = new MassiveList<>();
		
		// TODO: What about super classes?
		// TODO: While we not often use super classes they could in theory also be meant to be editable.
		// TODO: Something to consider coding in for the future.
		for (Field field : clazz.getDeclaredFields())
		{
			if ( ! isVisible(field)) continue;
			
			PropertyReflection<O, ?> property = get(field, typeObject);
			ret.add(property);
		}
		
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public static <O, V> PropertyReflection<O, V> get(final Field field, Type<O> typeObject)
	{
		Type<V> typeValue = (Type<V>) RegistryType.getType(field);
		
		// This makes sure it is called "volume for clickSound" instead of "volume for UiButtonClick 0.75 1.0"
		// We also only do it for things with properties, so the edited message works and shows the actual visual.
		if (typeValue.hasInnerProperties())
		{
			typeValue = new TypeWrapper<V>(typeValue)
			{
				@Override
				public Mson getVisualMsonInner(V value, CommandSender sender)
				{
					return Mson.mson(PropertyReflection.getName(field)).color(ChatColor.AQUA);
				}
				
				@Override
				public String getVisualInner(V value, CommandSender sender)
				{
					return ChatColor.AQUA + PropertyReflection.getName(field);
				}
			};
		}
		
		return new PropertyReflection<>(typeObject, typeValue, field);
	}
	
	public PropertyReflection(Type<O> typeObject, Type<V> typeValue, Field field)
	{
		super(typeObject, typeValue);
		
		ReflectionUtil.makeAccessible(field);
		this.field = field;
		this.getter = calcGetter(field);
		this.setter = calcSetter(field);
		
		this.setVisible(isVisible(field));
		this.setInheritable(isInheritable(field));
		this.setEditable(isEditable(field));
		this.setNullable(isNullable(field));
		this.setName(getName(field));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public V getRaw(O object)
	{
		// Use method if possible.
		if (this.getGetter() != null)
		{
			return ReflectionUtil.invokeMethod(this.getGetter(), object);
		}
		else
		{
			// Otherwise just get.
			return ReflectionUtil.getField(this.getField(), object);	
		}
	}
	
	@Override
	public O setRaw(O object, V value)
	{
		// Use method if possible.
		if (this.getSetter() != null)
		{
			ReflectionUtil.invokeMethod(this.getSetter(), object, value);
		}
		else
		{
			// Otherwise just set.
			ReflectionUtil.setField(this.getField(), object, value);
		}
		
		return object;
	}
	
	// -------------------------------------------- //
	// PROPERTY SETTINGS CALCULATION
	// -------------------------------------------- //
	
	public static boolean isVisible(Field field)
	{
		// Create
		boolean ret = VISIBLE_DEFAULT;
		
		// Fill > Standard
		int modifiers = field.getModifiers();
		if (Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers)) ret = false;
		
		// Fill > Annotation
		ret = getAnnotationValue(field, EditorVisible.class, ret);
		
		// Return
		return ret;
	}
	
	public static boolean isInheritable(Field field)
	{
		return getAnnotationValue(field, EditorInheritable.class, INHERITABLE_DEFAULT);
	}
	
	public static boolean isEditable(Field field)
	{
		// Create
		boolean ret = EDITABLE_DEFAULT;
		
		// Fill > Standard
		int modifiers = field.getModifiers();
		if (Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers) || Modifier.isFinal(modifiers)) ret = false;
		
		// Fill > Version
		if (field.getName().equals(MigratorUtil.VERSION_FIELD_NAME)) ret = false;
		
		// Fill > Annotation
		ret = getAnnotationValue(field, EditorEditable.class, ret);
		
		// Return
		return ret;
	}
	
	public static boolean isNullable(Field field)
	{
		// Primitive
		if (field.getType().isPrimitive()) return false;
		
		// Annotation
		return getAnnotationValue(field, EditorNullable.class, NULLABLE_DEFAULT);
	}
	
	
	public static String getName(Field field)
	{
		// Create
		String ret;
		
		// Fill > Standard
		ret = field.getName();
		
		// Fill > Annotation
		EditorName annotation = field.getAnnotation(EditorName.class);
		if (annotation != null) ret = annotation.value();
		
		// Return
		return ret;
	}
	
	// -------------------------------------------- //
	// ANNOTATION UTIL
	// -------------------------------------------- //
	
	public static <U> U getAnnotationValue(Field field, Class<? extends Annotation> clazz, U defaultValue)
	{
		// Try for field
		Annotation annotation = field.getAnnotation(clazz);
		if (annotation != null) return invokeAnnotationValue(annotation, clazz);
		
		// Try class
		annotation = field.getDeclaringClass().getAnnotation(clazz);
		if (annotation != null) return invokeAnnotationValue(annotation, clazz);
		
		return defaultValue;
	}
	
	private static <U> U invokeAnnotationValue(Annotation annotation, Class<?> clazz)
	{
		return ReflectionUtil.invokeMethod(ReflectionUtil.getMethod(clazz, "value"), annotation);
	}
	
}
