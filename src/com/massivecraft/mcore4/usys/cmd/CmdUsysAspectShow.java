package com.massivecraft.mcore4.usys.cmd;

import com.massivecraft.mcore4.Permission;
import com.massivecraft.mcore4.cmd.arg.ARAspect;
import com.massivecraft.mcore4.cmd.req.ReqHasPerm;
import com.massivecraft.mcore4.usys.Aspect;
import com.massivecraft.mcore4.util.Txt;

public class CmdUsysAspectShow extends UsysCommand
{
	public CmdUsysAspectShow()
	{
		this.addAliases("s", "show");
		this.addRequiredArg("aspect");
		
		this.addRequirements(ReqHasPerm.get(Permission.USYS_ASPECT_SHOW.node));
	}
	
	@Override
	public void perform()
	{
		Aspect aspect = this.arg(0, ARAspect.get());
		if (aspect == null) return;
		
		msg(Txt.titleize("Aspect: "+aspect.getId()));
		msg("<k>using multiverse: <v>%s",aspect.multiverse().getId());
		
		for (String descLine : aspect.getDesc())
		{
			msg(descLine);
		}
	}
}
