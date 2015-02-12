package com.massivecraft.massivecore.cmd.massivecore;

import com.massivecraft.massivecore.Aspect;
import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.Multiverse;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.arg.ARAspect;
import com.massivecraft.massivecore.cmd.arg.ARMultiverse;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

public class CmdMassiveCoreUsysAspectUse extends MassiveCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreUsysAspectUse()
	{
		// Aliases
		this.addAliases("u", "use");
		
		// Args
		this.addRequiredArg("aspect");
		this.addRequiredArg("multiverse");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MassiveCorePerm.USYS_ASPECT_USE.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		Aspect aspect = this.arg(0, ARAspect.get());
		Multiverse multiverse = this.arg(1, ARMultiverse.get());
		
		aspect.setMultiverse(multiverse);
		
		msg("<g>The aspect <h>%s<g> now use multiverse <h>%s<g>.", aspect.getId(), multiverse.getId());
	}
	
}
