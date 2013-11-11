package com.massivecraft.mcore.mcorecmd;

import com.massivecraft.mcore.ConfServer;
import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdMCoreId extends MCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMCoreId()
	{
		// Aliases
		this.addAliases("id");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_MCORE_ID.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		this.msg("<i>The id of this server is \"<h>%s<i>\".", ConfServer.serverid);
	}
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
}
