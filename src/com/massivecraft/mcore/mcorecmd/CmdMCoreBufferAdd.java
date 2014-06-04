package com.massivecraft.mcore.mcorecmd;

import com.massivecraft.mcore.MCoreEngineVariable;
import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdMCoreBufferAdd extends MCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMCoreBufferAdd()
	{
		// Aliases
		this.addAliases("a", "add");
		
		// Args
		this.addRequiredArg("string");
		this.setErrorOnToManyArgs(false);
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MCorePerm.BUFFER_ADD.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		String string = this.argConcatFrom(0);
		if (string == null) return;
		
		String buffer = MCoreEngineVariable.getBuffer(sender);
		buffer += string;
		MCoreEngineVariable.setBuffer(sender, buffer);
		
		msg("<i>Buffer Add");
	}
	
}
