package com.massivecraft.massivecore.command.editor;

import java.util.List;

import com.massivecraft.massivecore.MassiveException;

public class CommandEditContainerAdd<O, V> extends CommandEditContainerAbstract<O, V>
{	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CommandEditContainerAdd(EditSettings<O> settings, Property<O, V> property)
	{
		// Super
		super(settings, property);
		
		// Aliases
		this.addAliases("put");
		
		// Parameters
		this.addParametersElement(true);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void alter(List<Object> elements) throws MassiveException
	{
		// Args
		Object element = this.readElement();
		
		// Alter
		elements.add(element);
	}	
	
}
