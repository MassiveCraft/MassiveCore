package com.massivecraft.mcore4.usys.cmd;

import com.massivecraft.mcore4.Permission;
import com.massivecraft.mcore4.cmd.arg.ARAspect;
import com.massivecraft.mcore4.cmd.arg.ARMultiverse;
import com.massivecraft.mcore4.cmd.req.ReqHasPerm;
import com.massivecraft.mcore4.usys.Aspect;
import com.massivecraft.mcore4.usys.Multiverse;

public class CmdUsysAspectUse extends UsysCommand
{
	public CmdUsysAspectUse()
	{
		this.addAliases("u", "use");
		this.addRequiredArg("aspect");
		this.addRequiredArg("multiverse");
		
		this.addRequirements(ReqHasPerm.get(Permission.USYS_ASPECT_USE.node));
	}
	
	@Override
	public void perform()
	{
		Aspect aspect = this.arg(0, ARAspect.get());
		if (aspect == null) return;
		
		Multiverse multiverse = this.arg(1, ARMultiverse.get());
		if (multiverse == null) return;
		
		aspect.multiverse(multiverse);
		
		msg("<g>The aspect <h>%s<g> now use multiverse <h>%s<g>.", aspect.getId(), multiverse.getId());
	}
}
