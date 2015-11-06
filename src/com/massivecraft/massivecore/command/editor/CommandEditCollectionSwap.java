package com.massivecraft.massivecore.command.editor;

import java.util.Collection;
import java.util.List;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;

public class CommandEditCollectionSwap<O, V extends Collection<?>> extends CommandEditCollectionAbstract<O, V>
{	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CommandEditCollectionSwap(EditSettings<O> settings, Property<O, V> property)
	{
		// Super	
		super(settings, property);
		
		// Parameters
		this.addParameter(TypeInteger.get(), "indexOne");
		this.addParameter(TypeInteger.get(), "indexTwo");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public List<Object> alter(List<Object> list) throws MassiveException
	{
		// Args
		int indexOne = this.readArg();
		int indexTwo = this.readArg();
		
		// Alter
		Object one = list.get(indexOne);
		Object two = list.get(indexTwo);
		list.set(indexOne, two);
		list.set(indexTwo, one);
	
		// Return
		return list;
	}
	
}
