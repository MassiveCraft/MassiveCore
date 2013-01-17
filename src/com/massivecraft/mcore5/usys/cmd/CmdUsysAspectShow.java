package com.massivecraft.mcore5.usys.cmd;

import com.massivecraft.mcore5.Permission;
import com.massivecraft.mcore5.cmd.MCommand;
import com.massivecraft.mcore5.cmd.arg.ARAspect;
import com.massivecraft.mcore5.cmd.req.ReqHasPerm;
import com.massivecraft.mcore5.usys.Aspect;
import com.massivecraft.mcore5.util.Txt;

public class CmdUsysAspectShow extends MCommand
{
	public CmdUsysAspectShow()
	{
		this.addAliases("s", "show");
		this.addRequiredArg("aspect");
		
		this.addRequirements(ReqHasPerm.get(Permission.CMD_USYS_ASPECT_SHOW.node));
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
