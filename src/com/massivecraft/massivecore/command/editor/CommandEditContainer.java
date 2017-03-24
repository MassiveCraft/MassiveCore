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
		this.addChild(new CommandEditShow<>(settings, property));
		
		if (property.isNullable())
		{
			this.addChild(new CommandEditCreate<>(settings, property));
			this.addChild(new CommandEditDelete<>(settings, property));
		}
		
		if (property.isEditable())
		{
			this.addChild(new CommandEditContainerAdd<>(settings, property));
			
			// These are not suitable for maps.
			if (property.getValueType().isContainerCollection())
			{
				this.addChild(new CommandEditContainerInsert<>(settings, property));
				this.addChild(new CommandEditContainerSet<>(settings, property));
			}
			
			this.addChild(new CommandEditContainerRemove<>(settings, property));
			this.addChild(new CommandEditContainerRemoveIndex<>(settings, property));
			
			// The container must not be sorted, and must be ordered.
			if ( ! property.getValueType().isContainerSorted() && property.getValueType().getContainerComparator() == null && property.getValueType().isContainerOrdered())
			{
				this.addChild(new CommandEditContainerMove<>(settings, property));
				this.addChild(new CommandEditContainerSwap<>(settings, property));
			}
		
			this.addChild(new CommandEditContainerClear<>(settings, property));
		}
	}
	
}
