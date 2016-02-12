package com.massivecraft.massivecore.command.editor;

import com.massivecraft.massivecore.command.type.RegistryType;
import com.massivecraft.massivecore.command.type.Type;
import com.massivecraft.massivecore.command.type.TypeSingleton;

public class CommandEditSingleton<O> extends CommandEditReflection<O, O>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CommandEditSingleton(O object, String permission)
	{
		this(object, getType(object), permission);
	}
	
	@SuppressWarnings("unchecked")
	public CommandEditSingleton(O object, Type<O> typeObject, String permission)
	{
		super(EditSettingsSingleton.get(object, typeObject, permission), new PropertyThis<>(typeObject), (Class<O>) object.getClass());
		String name = typeObject.getName(object);
		this.setAliases(name);
		this.setDesc("edit " + name);
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	private static <O> Type<O> getType(O object)
	{
		// Get the return value
		Type<O> ret = TypeSingleton.get(object);
		
		// If no registered type exists. Use this one.
		if ( ! RegistryType.isRegistered(object.getClass()))
		{
			RegistryType.register((Class<O>) object.getClass(), ret);
		}
		return ret;
	}
	
}
