package com.massivecraft.massivecore.command.editor;

import java.util.Collection;
import java.util.List;

import com.massivecraft.massivecore.MassiveException;

public class CommandEditCollectionClear<O, V extends Collection<?>> extends CommandEditCollectionAbstract<O, V>
{	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CommandEditCollectionClear(EditSettings<O> settings, Property<O, V> property)
	{
		// Super	
		super(settings, property);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void alter(List<Object> list) throws MassiveException
	{
		// Apply
		list.clear();
	}
	
}
