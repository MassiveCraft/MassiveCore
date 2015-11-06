package com.massivecraft.massivecore.command.editor;

import java.util.Collection;
import java.util.List;

import com.massivecraft.massivecore.MassiveException;

public class CommandEditCollectionAdd<O, V extends Collection<?>> extends CommandEditCollectionAbstract<O, V>
{	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CommandEditCollectionAdd(EditSettings<O> settings, Property<O, V> property)
	{
		// Super	
		super(settings, property);
		
		// Parameters
		this.addParameter(this.getValueInnerType(), this.getProperty().getName(), true);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public List<Object> alter(List<Object> list) throws MassiveException
	{
		// Args
		Object element = this.readArg();
		
		// Alter
		list.add(element);
		
		// Return
		return list;
	}	
	
}
