package com.massivecraft.massivecore.cmd.massivecore;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.Multiverse;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.arg.ARMultiverse;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

public class CmdMassiveCoreUsysUniverseClear extends MassiveCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreUsysUniverseClear()
	{
		// Aliases
		this.addAliases("c", "clear");
		
		// Args
		this.addRequiredArg("universe");
		this.addRequiredArg("multiverse");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MassiveCorePerm.USYS_UNIVERSE_CLEAR.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		Multiverse multiverse = this.arg(1, ARMultiverse.get());
		
		String universe = this.arg(0);
		
		if (universe.equals(MassiveCore.DEFAULT))
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
