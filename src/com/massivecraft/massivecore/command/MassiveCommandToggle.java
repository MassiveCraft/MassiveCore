package com.massivecraft.massivecore.command;

import java.util.List;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeBoolean;
import com.massivecraft.massivecore.util.Txt;

public abstract class MassiveCommandToggle extends MassiveCommand
{
	// -------------------------------------------- //
	// FIELD
	// -------------------------------------------- //
	
	protected String info = null;
	public String getInfo() { return this.info; }
	public void setInfo(String info) { this.info = info; }
	public String getInfoSuffix()
	{
		String info = this.getInfo();
		if (info == null) return "";
		return " " + info;
	}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public MassiveCommandToggle()
	{
		// Parameters
		this.addParameter(TypeBoolean.getOn(), "on|off", "toggle");
	}
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract boolean getValue();
	
	public abstract void setValue(boolean value);
	
	// -------------------------------------------- //
	// GET NAME
	// -------------------------------------------- //
	
	public String getName()
	{
		List<String> aliases = this.getAliases();
		return Txt.upperCaseFirst(aliases.get(aliases.size() - 1));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Args
		boolean before = this.getValue();
		boolean after = this.readArg( ! before);
		String afterDesc = TypeBoolean.getOn().getVisual(after, sender);
		
		// NoChange
		if (after == before)
		{
			String message = Txt.parse("<h>%s<i> is already <h>%s<i>.", this.getName(), afterDesc);
			if (after) message += this.getInfoSuffix();
			message(message);
			return;
		}
		
		// Apply
		this.setValue(after);
		
		// Inform
		String message = Txt.parse("<h>%s<i> is now <h>%s<i>.", this.getName(), afterDesc);
		if (after) message += this.getInfoSuffix();
		message(message);
	}

}
