package com.massivecraft.massivecore.cmd;

import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.VisibilityMode;


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
		
		// Args
		this.setErrorOnTooManyArgs(false);
		
		// Visibility
		this.setVisibilityMode(VisibilityMode.INVISIBLE);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{	
		msg("<i>Use this new command instead:");
		sendMessage(target.getUseageTemplate(true));
	}
	
}
