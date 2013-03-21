package com.massivecraft.mcore.usys.cmd;

import com.massivecraft.mcore.Perm;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.arg.ARAspect;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.usys.Aspect;
import com.massivecraft.mcore.util.Txt;

public class CmdUsysAspectShow extends MCommand
{
	public CmdUsysAspectShow()
	{
		this.addAliases("s", "show");
		this.addRequiredArg("aspect");
		
		this.addRequirements(ReqHasPerm.get(Perm.CMD_USYS_ASPECT_SHOW.node));
	}
	
	@Override
	public void perform()
	{
		Aspect aspect = this.arg(0, ARAspect.get());
		if (aspect == null) return;
		
		msg(Txt.titleize("Aspect: "+aspect.getId()));
		msg("<k>using multiverse: <v>%s",aspect.getMultiverse().getId());
		
		for (String descLine : aspect.getDesc())
		{
			msg(descLine);
		}
	}
}
