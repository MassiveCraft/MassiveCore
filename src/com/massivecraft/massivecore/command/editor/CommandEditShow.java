package com.massivecraft.massivecore.command.editor;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Parameter;

public class CommandEditShow<O, V> extends CommandEditAbstract<O, V>
{	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CommandEditShow(EditSettings<O> settings, Property<O, V> property)
	{
		// Super	
		super(settings, property, false);
		
		// Aliases
		String alias = this.createCommandAlias();
		this.setAliases(alias);
		
		// Parameters
		this.addParameter(Parameter.getPage());
		
		// Desc
		this.setDesc(alias + " " + this.getPropertyName());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		int page = this.readArg();
		this.show(page);
	}
	
}
