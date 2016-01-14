package com.massivecraft.massivecore.command.editor;

import java.util.Collection;
import java.util.List;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;

public class CommandEditCollectionRemove<O, V extends Collection<?>> extends CommandEditCollectionAbstract<O, V>
{	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CommandEditCollectionRemove(EditSettings<O> settings, Property<O, V> property)
	{
		// Super	
		super(settings, property);
		
		// Parameters
		this.addParameter(TypeInteger.get(), "index");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void alter(List<Object> list) throws MassiveException
	{
		// Args
		int index = this.readArg();
		
		// Alter
		list.remove(index);
	}
	
}
