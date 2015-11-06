package com.massivecraft.massivecore.command.editor;

import java.util.Collection;

public class CommandEditCollection<O, V extends Collection<?>> extends CommandEditAbstract<O, V>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CommandEditCollection(EditSettings<O> settings, Property<O, V> property)
	{
		// Super
		super(settings, property, null);
		
		// Children
		this.addChild(new CommandEditShow<O, V>(settings, property));
		this.addChild(new CommandEditCreate<O, V>(settings, property));
		this.addChild(new CommandEditDelete<O, V>(settings, property));
		
		this.addChild(new CommandEditCollectionAdd<O, V>(settings, property));
		this.addChild(new CommandEditCollectionInsert<O, V>(settings, property));
		this.addChild(new CommandEditCollectionSet<O, V>(settings, property));
		this.addChild(new CommandEditCollectionRemove<O, V>(settings, property));
		this.addChild(new CommandEditCollectionMove<O, V>(settings, property));
		this.addChild(new CommandEditCollectionSwap<O, V>(settings, property));
		this.addChild(new CommandEditCollectionClear<O, V>(settings, property));
	}
	
}
