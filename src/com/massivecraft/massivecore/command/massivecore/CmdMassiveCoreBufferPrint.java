package com.massivecraft.massivecore.command.massivecore;

import com.massivecraft.massivecore.MassiveCoreEngineVariable;
import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;

public class CmdMassiveCoreBufferPrint extends MassiveCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreBufferPrint()
	{
		// Aliases
		this.addAliases("print");
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(MassiveCorePerm.BUFFER_PRINT.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		String buffer = MassiveCoreEngineVariable.getBuffer(sender);
		if (buffer == null || buffer.length() == 0)
		{
			msg("<i>Nothing to print. Your buffer is empty.");
			return;
		}
		
		msg("<i>Printing your buffer on the line below:");
		message(buffer);
	}
	
}
