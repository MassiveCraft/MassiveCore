package com.massivecraft.mcore.mcorecmd;

import java.util.List;

import com.massivecraft.mcore.MultiverseColl;
import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdMCoreUsysMultiverseNew extends MCoreCommand
{
	public CmdMCoreUsysMultiverseNew(List<String> aliases)
	{
		super(aliases);
		this.addRequiredArg("multiverse");
		
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_MCORE_USYS_MULTIVERSE_NEW.node));
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
