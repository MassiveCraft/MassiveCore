package com.massivecraft.massivecore.command.editor;

import java.util.List;

import com.massivecraft.massivecore.MassiveException;

public class CommandEditContainerClear<O, V> extends CommandEditContainerAbstract<O, V>
{	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CommandEditContainerClear(EditSettings<O> settings, Property<O, V> property)
	{
		// Super	
		super(settings, property);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void alter(List<Object> elements) throws MassiveException
	{
		// Apply
		elements.clear();
	}
	
}
