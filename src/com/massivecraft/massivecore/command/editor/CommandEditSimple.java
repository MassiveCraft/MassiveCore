package com.massivecraft.massivecore.command.editor;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.TypeNullable;
import com.massivecraft.massivecore.util.PermUtil;

public class CommandEditSimple<O, V> extends CommandEditAbstract<O, V>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CommandEditSimple(EditSettings<O> settings, Property<O, V> property)
	{
		// Super
		super(settings, property, null);
		
		// Parameters
		if (property.isEditable())
		{
			this.addParameter(TypeNullable.get(this.getProperty().getValueType()), "set", "show", true);	
		}
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Show
		if ( ! this.argIsSet(0))
		{
			this.show(0);
			return;
		}
		
		// Permission
		if ( ! PermUtil.has(sender, this.getPropertyPermission(), true)) return;
		
		// Arguments
		V after = this.readArg();
		
		// Validate
		if (after == null) this.requireNullable();
		
		// Apply
		this.attemptSet(after);
	}
	
}
