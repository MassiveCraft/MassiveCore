package com.massivecraft.mcore.usys.cmd;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.Multiverse;
import com.massivecraft.mcore.Perm;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.arg.ARMultiverse;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdUsysMultiverseDel extends MCommand
{
	public CmdUsysMultiverseDel()
	{
		this.addAliases("d", "del");
		this.addRequiredArg("multiverse");
		
		this.addRequirements(ReqHasPerm.get(Perm.CMD_USYS_MULTIVERSE_DEL.node));
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
