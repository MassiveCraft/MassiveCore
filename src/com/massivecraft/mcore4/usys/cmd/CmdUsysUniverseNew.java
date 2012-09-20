package com.massivecraft.mcore4.usys.cmd;

import com.massivecraft.mcore4.Permission;
import com.massivecraft.mcore4.cmd.arg.ARMultiverse;
import com.massivecraft.mcore4.cmd.req.ReqHasPerm;
import com.massivecraft.mcore4.usys.Multiverse;

public class CmdUsysUniverseNew extends UsysCommand
{
	public CmdUsysUniverseNew()
	{
		this.addAliases("n", "new");
		this.addRequiredArg("universe");
		this.addRequiredArg("multiverse");
		
		this.addRequirements(ReqHasPerm.get(Permission.USYS_UNIVERSE_NEW.node));
	}
	
	@Override
	public void perform()
	{
		Multiverse multiverse = this.arg(1, ARMultiverse.get());
		if (multiverse == null) return;
		
		String universe = this.arg(0);
		
		if (multiverse.containsUniverse(universe) || universe.equals(Multiverse.DEFAULT))
		{
			msg("<b>The universe <h>%s<b> already exists in multiverse <h>%s<b>.", universe, multiverse.getId());
			return;
		}
		
		multiverse.newUniverse(universe);
		
		msg("<g>Created universe <h>%s<g> in multiverse <h>%s<g>.", universe, multiverse.getId());
	}
}
