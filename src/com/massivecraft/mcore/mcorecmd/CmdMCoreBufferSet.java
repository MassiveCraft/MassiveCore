package com.massivecraft.mcore.mcorecmd;

import com.massivecraft.mcore.MCoreEngineVariable;
import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdMCoreBufferSet extends MCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMCoreBufferSet()
	{
		// Aliases
		this.addAliases("s", "set");
		
		// Args
		this.addRequiredArg("string");
		this.setErrorOnToManyArgs(false);
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MCorePerm.BUFFER_SET.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		String string = this.argConcatFrom(0);
		if (string == null) return;
		
		MCoreEngineVariable.setBuffer(sender, string);
		
		msg("<i>Buffer was Set");
	}
	
}
