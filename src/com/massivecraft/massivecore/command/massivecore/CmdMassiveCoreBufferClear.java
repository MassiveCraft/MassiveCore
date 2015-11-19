package com.massivecraft.massivecore.command.massivecore;

import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.engine.EngineMassiveCoreVariable;

public class CmdMassiveCoreBufferClear extends MassiveCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreBufferClear()
	{
		// Aliases
		this.addAliases("clear");
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(MassiveCorePerm.BUFFER_CLEAR.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		EngineMassiveCoreVariable.setBuffer(sender, "");
		
		msg("<i>Buffer Clear");
	}
	
}
