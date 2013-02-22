package com.massivecraft.mcore.usys.cmd;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.Permission;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.arg.ARMultiverse;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.usys.Multiverse;

public class CmdUsysUniverseDel extends MCommand
{
	public CmdUsysUniverseDel()
	{
		this.addAliases("d", "del");
		this.addRequiredArg("universe");
		this.addRequiredArg("multiverse");
		
		this.addRequirements(ReqHasPerm.get(Permission.CMD_USYS_MULTIVERSE_DEL.node));
	}
	
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
