package com.massivecraft.massivecore.command.massivecore;

import com.massivecraft.massivecore.ConfServer;
import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;

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
		this.addRequirements(RequirementHasPerm.get(MassiveCorePerm.ID.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		this.msg("<i>The id of this server is \"<h>%s<i>\".", ConfServer.serverid);
	}

}
