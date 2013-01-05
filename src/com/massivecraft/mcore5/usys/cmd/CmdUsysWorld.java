package com.massivecraft.mcore5.usys.cmd;

import com.massivecraft.mcore5.Permission;
import com.massivecraft.mcore5.cmd.arg.ARMultiverse;
import com.massivecraft.mcore5.cmd.req.ReqHasPerm;
import com.massivecraft.mcore5.usys.Multiverse;

public class CmdUsysWorld extends UsysCommand
{
	public CmdUsysWorld()
	{
		this.addAliases("w", "world");
		this.addRequiredArg("world");
		this.addRequiredArg("universe");
		this.addRequiredArg("multiverse");
		
		this.addRequirements(ReqHasPerm.get(Permission.CMD_USYS_WORLD.node));
	}
	
	@Override
	public void perform()
	{
		Multiverse multiverse = this.arg(2, ARMultiverse.get());
		if (multiverse == null) return;
		
		String universe = this.arg(1);
		String worldName = this.arg(0);
		
		if (!multiverse.containsUniverse(universe))
		{
			msg("<b>No universe <h>%s<b> exists in multiverse <h>%s<b>.", universe, multiverse.getId());
			return;
		}
		
		String universeOld = multiverse.getUniverseForWorldName(worldName);
		
		if (multiverse.setWorldUniverse(worldName, universe))
		{
			msg("<g>World <h>%s <g>moved from <h>%s <g>to <h>%s <g>universe in multiverse <h>%s<g>.", worldName, universeOld, universe, multiverse.getId());
		}
		else
		{
			msg("<i>World <h>%s <i>is already in universe <h>%s <i>in multiverse <h>%s<i>.", worldName, universe, multiverse.getId());
		}
	}
}
