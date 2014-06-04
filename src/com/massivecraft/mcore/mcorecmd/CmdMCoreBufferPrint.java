package com.massivecraft.mcore.mcorecmd;

import com.massivecraft.mcore.MCoreEngineVariable;
import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdMCoreBufferPrint extends MCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMCoreBufferPrint()
	{
		// Aliases
		this.addAliases("p", "print");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MCorePerm.BUFFER_PRINT.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		String buffer = MCoreEngineVariable.getBuffer(sender);
		if (buffer == null || buffer.length() == 0)
		{
			msg("<i>Nothing to print. Your buffer is empty.");
			return;
		}
		
		msg("<i>Printing your buffer on the line below:");
		sendMessage(buffer);
	}
	
}
