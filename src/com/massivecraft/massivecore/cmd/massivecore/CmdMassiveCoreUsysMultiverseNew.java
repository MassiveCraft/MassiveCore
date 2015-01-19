package com.massivecraft.massivecore.cmd.massivecore;

import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.MultiverseColl;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

public class CmdMassiveCoreUsysMultiverseNew extends MassiveCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreUsysMultiverseNew()
	{
		// Aliases
		this.addAliases("n", "new");
		
		// Args
		this.addRequiredArg("multiverse");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MassiveCorePerm.USYS_MULTIVERSE_NEW.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		String id = this.arg(0);
		
		if (MultiverseColl.get().containsId(id))
		{
			msg("<b>The multiverse <h>%s<b> already exists.", id);
			return;
		}
		
		MultiverseColl.get().create(id);
		
		msg("<g>Created multiverse <h>%s<g>.", id);
	}
	
}
