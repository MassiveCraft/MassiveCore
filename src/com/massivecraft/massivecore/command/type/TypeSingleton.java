package com.massivecraft.massivecore.command.type;

import com.massivecraft.massivecore.command.editor.CommandEditAbstract;
import com.massivecraft.massivecore.command.editor.CommandEditReflection;
import com.massivecraft.massivecore.command.editor.EditSettings;
import com.massivecraft.massivecore.command.editor.Property;

public class TypeSingleton<T> extends TypeAbstractChoice<T> 
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static <T> TypeSingleton<T> get(T singleton){ return new TypeSingleton<>(singleton); }
	public TypeSingleton(T singleton)
	{
		super.setAll(singleton);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getName()
	{
		return this.getAll().iterator().next().getClass().getSimpleName();
	}
	
	@Override
	public String getIdInner(T value)
	{
		return value.getClass().getSimpleName();
	}
	
	@SuppressWarnings("unchecked")
	public <O> CommandEditAbstract<O, T> createEditCommand(EditSettings<O> settings, Property<O, T> property)
	{
		return new CommandEditReflection<O, T>(settings, property, (Class<T>) this.getAll().iterator().next().getClass());
	}

}
