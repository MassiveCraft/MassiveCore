package com.massivecraft.massivecore.command.editor;

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
		
		// Aliases
		this.setAliases("swap");
		
		// Parameters
		this.addParameter(TypeInteger.get(), "indexOne");
		this.addParameter(TypeInteger.get(), "indexTwo");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void alter(List<Object> elements) throws MassiveException
	{
		// Args
		int indexOne = this.readArg();
		int indexTwo = this.readArg();
		
		// Alter
		Object elementOne = elements.get(indexOne);
		Object elementTwo = elements.get(indexTwo);
		elements.set(indexOne, elementTwo);
		elements.set(indexTwo, elementOne);
	}
	
}
