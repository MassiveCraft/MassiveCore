package com.massivecraft.massivecore.command.editor;

import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.Type;

public class CommandEditProperties<O, V> extends CommandEditAbstract<O, V>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CommandEditProperties(EditSettings<O> settings, Property<O, V> property, String permission)
	{
		// Super
		super(settings, property, null);
		
		this.addChild(new CommandEditShow<>(settings, property));
		
		// Parameters
		if (property.isEditable())
		{
			Type<V> type = this.getValueType();
			EditSettings<V> fieldSettings = new EditSettingsDelegate<>(settings, property);
			for (Property<V, ?> prop : type.getInnerProperties())
			{
				this.addChild(prop.createEditCommand(fieldSettings));
			}
		}
		
		if (permission != null) this.addRequirements(RequirementHasPerm.get(permission));
	}

}
