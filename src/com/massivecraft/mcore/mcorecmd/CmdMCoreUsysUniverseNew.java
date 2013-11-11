package com.massivecraft.mcore.mcorecmd;

import com.massivecraft.mcore.Multiverse;
import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.arg.ARMultiverse;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdMCoreUsysUniverseNew extends MCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMCoreUsysUniverseNew()
	{
		// Aliases
		this.addAliases("n", "new");
		
		// Args
		this.addRequiredArg("universe");
		this.addRequiredArg("multiverse");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_MCORE_USYS_UNIVERSE_NEW.node));
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
		
		if (multiverse.containsUniverse(universe))
		{
			msg("<b>The universe <h>%s<b> already exists in multiverse <h>%s<b>.", universe, multiverse.getId());
			return;
		}
		
		multiverse.newUniverse(universe);
		
		msg("<g>Created universe <h>%s<g> in multiverse <h>%s<g>.", universe, multiverse.getId());
	}
	
}
