package com.massivecraft.mcore.mcorecmd;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.Multiverse;
import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.arg.ARMultiverse;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdMCoreUsysMultiverseDel extends MCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMCoreUsysMultiverseDel()
	{
		// Aliases
		this.addAliases("d", "del");
		
		// Args
		this.addRequiredArg("multiverse");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_MCORE_USYS_MULTIVERSE_DEL.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		Multiverse multiverse = this.arg(0, ARMultiverse.get());
		if (multiverse == null) return;
		
		String id = multiverse.getId();
		
		if (id.equals(MCore.DEFAULT))
		{
			msg("<b>You can't delete the default multiverse.");
			return;
		}
		
		multiverse.detach();
		
		msg("<g>Deleted multiverse <h>%s<g>.", id);
	}
	
}
