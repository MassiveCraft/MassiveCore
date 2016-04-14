package com.massivecraft.massivecore.command.editor;

import java.lang.reflect.Field;

import com.massivecraft.massivecore.command.MassiveCommand;

public class CommandEditReflection<O, V> extends CommandEditAbstract<O, V>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CommandEditReflection(EditSettings<O> settings, Property<O, V> property, Class<V> clazz)
	{
		super(settings, property, null);
		
		// TODO: What about super classes?
		// TODO: While we not often use super classes they could in theory also be meant to be editable.
		// TODO: Something to consider coding in for the future.
		for (Field field : clazz.getDeclaredFields())
		{
			if ( ! PropertyReflection.isVisible(field)) continue;
			Property<O, ?> fieldProperty = PropertyReflection.get(field);
			MassiveCommand fieldCommand = fieldProperty.createEditCommand(settings);
			this.addChild(fieldCommand);
		}
		
	}

}
