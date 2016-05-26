package com.massivecraft.massivecore.command.editor;

import com.massivecraft.massivecore.command.type.Type;

public class CommandEditProperties<O, V> extends CommandEditAbstract<O, V>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CommandEditProperties(EditSettings<O> parentSettings, Property<O, V> childProperty)
	{
		// Super
		super(parentSettings, childProperty, null);
		
		this.addChild(new CommandEditShow<>(parentSettings, childProperty));
		
		// Parameters
		if (childProperty.isEditable())
		{
			Type<V> type = this.getValueType();
			EditSettings<V> childSettings = new EditSettings<>(parentSettings, childProperty);
			for (Property<V, ?> prop : type.getInnerProperties())
			{
				this.addChild(prop.createEditCommand(childSettings));
			}
		}
	}

}
