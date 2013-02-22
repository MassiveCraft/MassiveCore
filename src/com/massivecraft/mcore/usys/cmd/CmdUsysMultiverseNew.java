package com.massivecraft.mcore.usys.cmd;

import com.massivecraft.mcore.Permission;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.usys.MultiverseColl;

public class CmdUsysMultiverseNew extends MCommand
{
	public CmdUsysMultiverseNew()
	{
		this.addAliases("n", "new");
		this.addRequiredArg("multiverse");
		
		this.addRequirements(ReqHasPerm.get(Permission.CMD_USYS_MULTIVERSE_NEW.node));
	}
	
	@Override
	public void perform()
	{
		String id = this.arg(0);
		
		if (MultiverseColl.i.containsId(id))
		{
			msg("<b>The multiverse <h>%s<b> already exists.", id);
			return;
		}
		
		MultiverseColl.i.create(id);
		
		msg("<g>Created multiverse <h>%s<g>.", id);
	}
}
