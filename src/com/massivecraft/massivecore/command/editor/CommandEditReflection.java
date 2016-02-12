package com.massivecraft.massivecore.command.editor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class CommandEditReflection<O, V> extends CommandEditAbstract<O, V>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CommandEditReflection(EditSettings<O> settings, Property<O, V> property, Class<V> clazz)
	{
		super(settings, property, null);
		for (Field field : clazz.getDeclaredFields())
		{
			if ( ! this.isOkay(field)) continue;
			Property<O, ?> propertyReflection = PropertyReflection.get(field);
			this.addChild(propertyReflection.createEditCommand(settings));
		}
	}
	
	// -------------------------------------------- //
	// OKAY
	// -------------------------------------------- //
	
	public boolean isOkay(Field field)
	{
		if ( ! this.isModifiersOkay(field.getModifiers())) return false;
		return true;
	}

	public boolean isModifiersOkay(int modifiers)
	{
		if (Modifier.isStatic(modifiers)) return false;
		if (Modifier.isVolatile(modifiers)) return false;
		return true;
	}

}
