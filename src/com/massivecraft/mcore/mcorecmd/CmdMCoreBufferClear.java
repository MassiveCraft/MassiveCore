package com.massivecraft.mcore.mcorecmd;

import com.massivecraft.mcore.EngineMCoreVariable;
import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdMCoreBufferClear extends MCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMCoreBufferClear()
	{
		// Aliases
		this.addAliases("c", "clear");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MCorePerm.BUFFER_CLEAR.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		EngineMCoreVariable.setBuffer(sender, "");
		
		msg("<i>Buffer Clear");
	}
	
}
