package com.massivecraft.massivecore.command.editor;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;

import java.util.List;

public class CommandEditContainerInsert<O, V> extends CommandEditContainerAbstract<O, V>
{	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CommandEditContainerInsert(EditSettings<O> settings, Property<O, V> property)
	{
		// Super	
		super(settings, property);
		
		// Parameters
		this.addParameter(TypeInteger.get(), "index");
		this.addParametersElement(true);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void alterElements(List<Object> elements) throws MassiveException
	{
		// Args
		int index = this.readArg();
		Object element = this.readElement();
		
		// Alter
		elements.add(index, element);
	}
	
}
