package com.massivecraft.mcore.mcorecmd;

import java.util.List;

import com.massivecraft.mcore.Multiverse;
import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.arg.ARMultiverse;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdMCoreUsysWorld extends MCoreCommand
{
	public CmdMCoreUsysWorld(List<String> aliases)
	{
		super(aliases);
		this.addRequiredArg("world");
		this.addRequiredArg("universe");
		this.addRequiredArg("multiverse");
		
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_MCORE_USYS_WORLD.node));
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
