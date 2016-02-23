package com.massivecraft.massivecore.command.editor;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.util.ContainerUtil;

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
	public void alter(V container) throws MassiveException
	{
		// Apply
		ContainerUtil.clear(container);
	}
	
}
