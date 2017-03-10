package com.massivecraft.massivecore.cmd;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.Multiverse;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.command.type.store.TypeMultiverse;

public class CmdMassiveCoreUsysUniverseClear extends MassiveCoreCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreUsysUniverseClear()
	{
		// Parameters
		this.addParameter(TypeString.get(), "universe").setDesc("the universe to clear");
		this.addParameter(TypeMultiverse.get(), "multiverse").setDesc("the multiverse of the universe to clear");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		String universe = this.readArg();
		Multiverse multiverse = this.readArg();
		
		if (universe.equals(MassiveCore.DEFAULT))
		{
			msg("<b>You can't clear the default universe.");
			msg("<b>It contains the worlds that aren't assigned to a universe.");
			return;
		}
		
		if (multiverse.clearUniverse(universe))
		{
			msg("<g>Cleared universe <h>%s<g> in multiverse <h>%s<g>.", universe, multiverse.getId());
		}
		else
		{
			msg("<b>No universe <h>%s<b> exists in multiverse <h>%s<b>.", universe, multiverse.getId());
		}
	}
	
}
