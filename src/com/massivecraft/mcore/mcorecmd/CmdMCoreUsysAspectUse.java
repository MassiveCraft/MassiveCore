package com.massivecraft.mcore.mcorecmd;

import java.util.List;

import com.massivecraft.mcore.Aspect;
import com.massivecraft.mcore.Multiverse;
import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.arg.ARAspect;
import com.massivecraft.mcore.cmd.arg.ARMultiverse;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdMCoreUsysAspectUse extends MCoreCommand
{
	public CmdMCoreUsysAspectUse(List<String> aliases)
	{
		super(aliases);
		this.addRequiredArg("aspect");
		this.addRequiredArg("multiverse");
		
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_MCORE_USYS_ASPECT_USE.node));
	}
	
	@Override
	public void perform()
	{
		Aspect aspect = this.arg(0, ARAspect.get());
		if (aspect == null) return;
		
		Multiverse multiverse = this.arg(1, ARMultiverse.get());
		if (multiverse == null) return;
		
		aspect.setMultiverse(multiverse);
		
		msg("<g>The aspect <h>%s<g> now use multiverse <h>%s<g>.", aspect.getId(), multiverse.getId());
	}
}
