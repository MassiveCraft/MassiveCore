package com.massivecraft.massivecore.command.editor;

public class CommandEditContainer<O, V> extends CommandEditAbstract<O, V>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CommandEditContainer(EditSettings<O> settings, Property<O, V> property)
	{
		// Super
		super(settings, property, null);
		
		// Children
		this.addChild(new CommandEditShow<O, V>(settings, property));
		
		if (property.isNullable())
		{
			this.addChild(new CommandEditCreate<O, V>(settings, property));
			this.addChild(new CommandEditDelete<O, V>(settings, property));
		}
		
		if (property.isEditable())
		{
			this.addChild(new CommandEditContainerAdd<O, V>(settings, property));
			
			// These are not suitable for maps.
			if (property.getValueType().isContainerCollection())
			{
				this.addChild(new CommandEditContainerInsert<O, V>(settings, property));
				this.addChild(new CommandEditContainerSet<O, V>(settings, property));	
			}
			
			this.addChild(new CommandEditContainerRemove<O, V>(settings, property));
			this.addChild(new CommandEditContainerRemoveIndex<O, V>(settings, property));
			
			// The container must not be sorted, and must be ordered.
			if ( ! property.getValueType().isContainerSorted() && property.getValueType().getContainerComparator() == null && property.getValueType().isContainerOrdered())
			{
				this.addChild(new CommandEditContainerMove<O, V>(settings, property));
				this.addChild(new CommandEditContainerSwap<O, V>(settings, property));
			}
		
			this.addChild(new CommandEditContainerClear<O, V>(settings, property));
		}
	}
	
}
