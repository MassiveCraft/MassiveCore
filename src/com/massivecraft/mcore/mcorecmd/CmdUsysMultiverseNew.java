package com.massivecraft.mcore.mcorecmd;

import com.massivecraft.mcore.MultiverseColl;
import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdUsysMultiverseNew extends MCommand
{
	public CmdUsysMultiverseNew()
	{
		this.addAliases("n", "new");
		this.addRequiredArg("multiverse");
		
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_USYS_MULTIVERSE_NEW.node));
	}
	
	@Override
	public void perform()
	{
		String id = this.arg(0);
		
		if (MultiverseColl.get().containsId(id))
		{
			msg("<b>The multiverse <h>%s<b> already exists.", id);
			return;
		}
		
		MultiverseColl.get().create(id);
		
		msg("<g>Created multiverse <h>%s<g>.", id);
	}
}
