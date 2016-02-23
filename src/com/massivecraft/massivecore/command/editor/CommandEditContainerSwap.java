package com.massivecraft.massivecore.command.editor;

import java.util.Collections;
import java.util.List;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;

public class CommandEditContainerSwap<O, V> extends CommandEditContainerAbstract<O, V>
{	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CommandEditContainerSwap(EditSettings<O> settings, Property<O, V> property)
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
	public void alterElements(List<Object> elements) throws MassiveException
	{
		// Args
		int indexOne = this.readArg();
		int indexTwo = this.readArg();
		
		// Alter
		Collections.swap(elements, indexOne, indexTwo);
	}
	
}
