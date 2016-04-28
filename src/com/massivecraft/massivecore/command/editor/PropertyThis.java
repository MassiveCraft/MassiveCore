package com.massivecraft.massivecore.command.editor;

import com.massivecraft.massivecore.command.type.Type;

public class PropertyThis<O> extends Property<O, O>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public PropertyThis(Type<O> objectType)
	{
		super(objectType, objectType, objectType.getName());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public O getRaw(O object)
	{
		return object;
	}

	@Override
	public O setRaw(O object, O value)
	{
		return object;
	}

}
