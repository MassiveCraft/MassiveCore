package com.massivecraft.massivecore.command.editor;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.Type;

import java.util.Map;

public class CommandEditMapRemove<O, V extends Map<?,?>> extends CommandEditMapAbstract<O, V>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CommandEditMapRemove(EditSettings<O> settings, Property<O, V> property, Type<?> mapValueType)
	{
		// Super
		super(settings, property, mapValueType);

		// Parameters
		this.addParameter(this.getMapKeyType(), this.getMapKeyType().getTypeName(), true);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Map<Object, Object> alter(Map<Object, Object> map) throws MassiveException
	{
		// Args
		Object key = this.readArg();

		// Alter
		map.remove(key);

		// Return
		return map;
	}
	
}
