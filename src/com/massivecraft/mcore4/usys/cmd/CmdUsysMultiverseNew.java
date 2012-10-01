package com.massivecraft.mcore4.usys.cmd;

import com.massivecraft.mcore4.Permission;
import com.massivecraft.mcore4.cmd.req.ReqHasPerm;
import com.massivecraft.mcore4.usys.MultiverseColl;

public class CmdUsysMultiverseNew extends UsysCommand
{
	public CmdUsysMultiverseNew()
	{
		this.addAliases("n", "new");
		this.addRequiredArg("multiverse");
		
		this.addRequirements(ReqHasPerm.get(Permission.USYS_MULTIVERSE_NEW.node));
	}
	
	@Override
	public void perform()
	{
		String id = this.arg(0);
		
		if (MultiverseColl.i.getIds().contains(id))
		{
			msg("<b>The multiverse <h>%s<b> alread exists.", id);
			return;
		}
		
		MultiverseColl.i.create(id);
		
		msg("<g>Created multiverse <h>%s<g>.", id);
	}
}
