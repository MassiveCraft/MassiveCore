package com.massivecraft.massivecore.cmd.massivecore;

import com.massivecraft.massivecore.Aspect;
import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.arg.ARAspect;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;

public class CmdMassiveCoreUsysAspectShow extends MassiveCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreUsysAspectShow()
	{
		// Aliases
		this.addAliases("s", "show");
		
		// Args
		this.addRequiredArg("aspect");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MassiveCorePerm.USYS_ASPECT_SHOW.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		Aspect aspect = this.arg(0, ARAspect.get());
		
		msg(Txt.titleize("Aspect: "+aspect.getId()));
		msg("<k>using multiverse: <v>%s",aspect.getMultiverse().getId());
		
		for (String descLine : aspect.getDesc())
		{
			msg(descLine);
		}
	}
	
}
