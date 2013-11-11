package com.massivecraft.mcore.mcorecmd;

import com.massivecraft.mcore.Aspect;
import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.arg.ARAspect;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.util.Txt;

public class CmdMCoreUsysAspectShow extends MCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMCoreUsysAspectShow()
	{
		// Aliases
		this.addAliases("s", "show");
		
		// Args
		this.addRequiredArg("aspect");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_MCORE_USYS_ASPECT_SHOW.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
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
