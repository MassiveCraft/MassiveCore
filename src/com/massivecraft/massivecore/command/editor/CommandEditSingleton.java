package com.massivecraft.massivecore.command.editor;

import com.massivecraft.massivecore.command.type.RegistryType;
import com.massivecraft.massivecore.command.type.Type;
import com.massivecraft.massivecore.command.type.TypeSingleton;

public class CommandEditSingleton<O> extends CommandEditProperties<O, O>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CommandEditSingleton(O object)
	{
		this(object, getType(object));
	}
	
	public CommandEditSingleton(O object, Type<O> typeObject)
	{
		super(createEditSettings(object, typeObject), new PropertyThis<>(typeObject));
		String name = typeObject.getName(object);
		this.setAliases(name);
		this.setDesc("edit " + name);
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	private static <O> EditSettings<O> createEditSettings(O object, Type<O> typeObject)
	{
		EditSettings<O> ret = new EditSettings<>(typeObject);
		
		PropertyUsed<O> usedProperty = new PropertyUsed<>(ret, object);
		ret.setUsedProperty(usedProperty);
		
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	private static <O> Type<O> getType(O object)
	{
		if (object == null) throw new NullPointerException("object");

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
