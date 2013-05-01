package com.massivecraft.mcore.mcorecmd;

import java.util.List;

import com.massivecraft.mcore.Aspect;
import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.arg.ARAspect;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.util.Txt;

public class CmdMCoreUsysAspectShow extends MCoreCommand
{
	public CmdMCoreUsysAspectShow(List<String> aliases)
	{
		super(aliases);
		this.addRequiredArg("aspect");
		
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_MCORE_USYS_ASPECT_SHOW.node));
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
