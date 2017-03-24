package com.massivecraft.massivecore.command;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeBooleanOn;
import com.massivecraft.massivecore.util.Txt;

import java.util.List;

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
		this.addParameter(TypeBooleanOn.get(), "on|off", "toggle");
	}
	
	// -------------------------------------------- //
	// VALUE
	// -------------------------------------------- //
	
	public abstract boolean getValue() throws MassiveException;
	
	public abstract void setValue(boolean value) throws MassiveException;
	
	// -------------------------------------------- //
	// NAME
	// -------------------------------------------- //
	
	public String getName() throws MassiveException
	{
		List<String> aliases = this.getAliases();
		return Txt.upperCaseFirst(aliases.get(aliases.size() - 1));
	}
	
	// -------------------------------------------- //
	// TARGET
	// -------------------------------------------- //
	
	public boolean readTarget(boolean before) throws MassiveException
	{
		return this.readArg(!before);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Args
		boolean before = this.getValue();
		boolean after = this.readTarget(before);
		String afterDesc = TypeBooleanOn.get().getVisual(after, sender);
		
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
