package com.massivecraft.massivecore.command.editor;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.Type;

import java.util.Map;

public class CommandEditMapClear<O, V extends Map<?,?>> extends CommandEditMapAbstract<O, V>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CommandEditMapClear(EditSettings<O> settings, Property<O, V> property, Type<?> mapValueType)
	{
		// Super	
		super(settings, property, mapValueType);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Map<Object, Object> alter(Map<Object, Object> map) throws MassiveException
	{
		// Alter
		map.clear();

		// Return
		return map;
	}
	
}
