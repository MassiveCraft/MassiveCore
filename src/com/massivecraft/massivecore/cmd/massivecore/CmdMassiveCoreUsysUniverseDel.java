package com.massivecraft.massivecore.cmd.massivecore;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.Multiverse;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.arg.ARMultiverse;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

public class CmdMassiveCoreUsysUniverseDel extends MassiveCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreUsysUniverseDel()
	{
		// Aliases
		this.addAliases("d", "del");
		
		// Args
		this.addRequiredArg("universe");
		this.addRequiredArg("multiverse");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MassiveCorePerm.USYS_MULTIVERSE_DEL.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		Multiverse multiverse = this.arg(1, ARMultiverse.get());
		
		String universe = this.arg(0);
		
		if (universe.equals(MassiveCore.DEFAULT))
		{
			msg("<b>You can't remove the default universe.");
			msg("<b>Each multiverse contains a default universe.");
			return;
		}
		
		if (!multiverse.containsUniverse(universe))
		{
			msg("<b>No universe <h>%s<b> exists in multiverse <h>%s<b>.", universe, multiverse.getId());
			return;
		}
		
		multiverse.delUniverse(universe);
		
		msg("<g>Deleted universe <h>%s<g> in multiverse <h>%s<g>.", universe, multiverse.getId());
	}
	
}
