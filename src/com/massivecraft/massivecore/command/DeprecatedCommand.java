package com.massivecraft.massivecore.command;

import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.Visibility;


public class DeprecatedCommand extends MassiveCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public MassiveCommand target;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public DeprecatedCommand(MassiveCommand target, String... aliases)
	{
		// Fields
		this.target = target;
		
		// Aliases
		this.setAliases(aliases);
		
		// Parameters
		this.setOverflowSensitive(false);
		
		// Visibility
		this.setVisibility(Visibility.INVISIBLE);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{	
		msg("<i>Use this new command instead:");
		message(target.getTemplate(true));
	}
	
}
