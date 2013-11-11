package com.massivecraft.mcore.mcorecmd;

import com.massivecraft.mcore.MultiverseColl;
import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdMCoreUsysMultiverseNew extends MCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMCoreUsysMultiverseNew()
	{
		// Aliases
		this.addAliases("n", "new");
		
		// Args
		this.addRequiredArg("multiverse");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_MCORE_USYS_MULTIVERSE_NEW.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		String id = this.arg(0);
		
		if (MultiverseColl.get().containsId(id))
		{
			msg("<b>The multiverse <h>%s<b> already exists.", id);
			return;
		}
		
		MultiverseColl.get().create(id);
		
		msg("<g>Created multiverse <h>%s<g>.", id);
	}
	
}
