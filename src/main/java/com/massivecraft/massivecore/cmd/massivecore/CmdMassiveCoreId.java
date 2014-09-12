package com.massivecraft.massivecore.cmd.massivecore;

import com.massivecraft.massivecore.ConfServer;
import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

public class CmdMassiveCoreId extends MassiveCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreId()
	{
		// Aliases
		this.addAliases("id");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MassiveCorePerm.ID.node));
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
