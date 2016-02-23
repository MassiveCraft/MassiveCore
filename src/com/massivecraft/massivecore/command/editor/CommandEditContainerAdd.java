package com.massivecraft.massivecore.command.editor;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.util.ContainerUtil;

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
	public void alter(V container) throws MassiveException
	{
		// Args
		Object element = this.readElement();
		
		// Alter
		ContainerUtil.addElement(container, element);
	}	
	
}
