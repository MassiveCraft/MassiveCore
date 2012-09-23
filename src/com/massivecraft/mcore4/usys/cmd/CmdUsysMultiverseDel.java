package com.massivecraft.mcore4.usys.cmd;

import com.massivecraft.mcore4.MCore;
import com.massivecraft.mcore4.Permission;
import com.massivecraft.mcore4.cmd.arg.ARMultiverse;
import com.massivecraft.mcore4.cmd.req.ReqHasPerm;
import com.massivecraft.mcore4.usys.Multiverse;

public class CmdUsysMultiverseDel extends UsysCommand
{
	public CmdUsysMultiverseDel()
	{
		this.addAliases("d", "del");
		this.addRequiredArg("multiverse");
		
		this.addRequirements(ReqHasPerm.get(Permission.USYS_MULTIVERSE_DEL.node));
	}
	
	@Override
	public void perform()
	{
		Multiverse multiverse = this.arg(0, ARMultiverse.get());
		if (multiverse == null) return;
		
		String id = multiverse.getId();
		
		if (id.equals(MCore.DEFAULT))
		{
			msg("<b>You can't delete the default multiverse.");
			return;
		}
		
		multiverse.detach();
		
		msg("<g>Deleted multiverse <h>%s<g>.", id);
	}
}
