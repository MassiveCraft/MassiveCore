package com.massivecraft.massivecore.command.editor;

import com.massivecraft.massivecore.MassiveException;

import java.util.Map;

public class CommandEditMapClear<O, V extends Map<?,?>> extends CommandEditMapAbstract<O, V>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CommandEditMapClear(EditSettings<O> settings, Property<O, V> property)
	{
		// Super	
		super(settings, property);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void alter(Map<Object, Object> map) throws MassiveException
	{
		// Alter
		map.clear();
	}
	
}
