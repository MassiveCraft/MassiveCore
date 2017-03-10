package com.massivecraft.massivecore.cmd;

import com.massivecraft.massivecore.ConfServer;

public class CmdMassiveCoreId extends MassiveCoreCommand
{
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		this.msg("<i>The id of this server is \"<h>%s<i>\".", ConfServer.serverid);
	}

}
