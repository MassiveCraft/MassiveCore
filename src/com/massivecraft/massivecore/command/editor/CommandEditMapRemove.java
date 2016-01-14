package com.massivecraft.massivecore.command.editor;

import com.massivecraft.massivecore.MassiveException;

import java.util.Map;

public class CommandEditMapRemove<O, V extends Map<?,?>> extends CommandEditMapAbstract<O, V>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CommandEditMapRemove(EditSettings<O> settings, Property<O, V> property)
	{
		// Super
		super(settings, property);

		// Parameters
		this.addParameter(this.getMapKeyType(), this.getMapKeyType().getTypeName(), true);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void alter(Map<Object, Object> map) throws MassiveException
	{
		// Args
		Object key = this.readArg();

		// Alter
		map.remove(key);
	}
	
}
