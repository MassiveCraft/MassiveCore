package com.massivecraft.mcore.mcorecmd;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.Multiverse;
import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.arg.ARMultiverse;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdMCoreUsysUniverseDel extends MCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMCoreUsysUniverseDel()
	{
		// Aliases
		this.addAliases("d", "del");
		
		// Args
		this.addRequiredArg("universe");
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
		Multiverse multiverse = this.arg(1, ARMultiverse.get());
		if (multiverse == null) return;
		
		String universe = this.arg(0);
		
		if (universe.equals(MCore.DEFAULT))
		{
			msg("<b>You can't remove the default universe.");
			msg("<b>Each multiverse contains a default universe.");
			return;
		}
		
		if (!multiverse.containsUniverse(universe))
		{
			msg("<b>No universe <h>%s<b> exists in multiverse <h>%s<b>.", universe, multiverse.getId());
			return;
		}
		
		multiverse.delUniverse(universe);
		
		msg("<g>Deleted universe <h>%s<g> in multiverse <h>%s<g>.", universe, multiverse.getId());
	}
	
}
