package com.massivecraft.mcore5.usys.cmd;

import com.massivecraft.mcore5.MCore;
import com.massivecraft.mcore5.Permission;
import com.massivecraft.mcore5.cmd.arg.ARMultiverse;
import com.massivecraft.mcore5.cmd.req.ReqHasPerm;
import com.massivecraft.mcore5.usys.Multiverse;

public class CmdUsysMultiverseDel extends UsysCommand
{
	public CmdUsysMultiverseDel()
	{
		this.addAliases("d", "del");
		this.addRequiredArg("multiverse");
		
		this.addRequirements(ReqHasPerm.get(Permission.CMD_USYS_MULTIVERSE_DEL.node));
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
