package com.massivecraft.mcore4.usys.cmd;

import com.massivecraft.mcore4.Permission;
import com.massivecraft.mcore4.cmd.arg.ARMultiverse;
import com.massivecraft.mcore4.cmd.req.ReqHasPerm;
import com.massivecraft.mcore4.usys.Multiverse;

public class CmdUsysUniverseClear extends UsysCommand
{
	public CmdUsysUniverseClear()
	{
		this.addAliases("c", "clear");
		this.addRequiredArg("universe");
		this.addRequiredArg("multiverse");
		
		this.addRequirements(ReqHasPerm.get(Permission.USYS_UNIVERSE_CLEAR.node));
	}
	
	@Override
	public void perform()
	{
		Multiverse multiverse = this.arg(1, ARMultiverse.get());
		if (multiverse == null) return;
		
		String universe = this.arg(0);
		
		if (universe.equals(Multiverse.DEFAULT))
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
