package com.massivecraft.massivecore.command.editor;

import java.util.Collection;
import java.util.List;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;

public class CommandEditCollectionMove<O, V extends Collection<?>> extends CommandEditCollectionAbstract<O, V>
{	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CommandEditCollectionMove(EditSettings<O> settings, Property<O, V> property)
	{
		// Super	
		super(settings, property);

		// Parameters
		this.addParameter(TypeInteger.get(), "indexFrom");
		this.addParameter(TypeInteger.get(), "indexTo");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public List<Object> alter(List<Object> list) throws MassiveException
	{
		// Args
		int indexFrom = this.readArg();
		int indexTo = this.readArg();
		
		// Alter
		Object element = list.remove(indexFrom);
		list.add(indexTo, element);
	
		// Return
		return list;
	}
	
}
