package com.massivecraft.mcore.mcorecmd;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.Multiverse;
import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.arg.ARMultiverse;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdMCoreUsysUniverseClear extends MCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMCoreUsysUniverseClear()
	{
		// Aliases
		this.addAliases("c", "clear");
		
		// Args
		this.addRequiredArg("universe");
		this.addRequiredArg("multiverse");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_MCORE_USYS_UNIVERSE_CLEAR.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		Multiverse multiverse = this.arg(1, ARMultiverse.get());
		if (multiverse == null) return;
		
		String universe = this.arg(0);
		
		if (universe.equals(MCore.DEFAULT))
		{
			msg("<b>You can't clear the default universe.");
			msg("<b>It contains the worlds that aren't assigned to a universe.");
			return;
		}
		
		if (multiverse.clearUniverse(universe))
		{
			msg("<g>Cleared universe <h>%s<g> in multiverse <h>%s<g>.", universe, multiverse.getId());
		}
		else
		{
			msg("<b>No universe <h>%s<b> exists in multiverse <h>%s<b>.", universe, multiverse.getId());
		}
	}
	
}
