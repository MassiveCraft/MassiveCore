package com.massivecraft.massivecore.command.editor;

import java.util.Collection;
import java.util.List;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;

public class CommandEditCollectionSet<O, V extends Collection<?>> extends CommandEditCollectionAbstract<O, V>
{	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CommandEditCollectionSet(EditSettings<O> settings, Property<O, V> property)
	{
		// Super	
		super(settings, property);
		
		// Parameters
		this.addParameter(TypeInteger.get(), "index");
		this.addParameter(this.getValueInnerType(), this.getProperty().getName(), true);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void alter(List<Object> list) throws MassiveException
	{
		// Args
		int index = this.readArg();
		Object element = this.readArg();
		
		// Alter
		list.set(index, element);
	}
	
}
