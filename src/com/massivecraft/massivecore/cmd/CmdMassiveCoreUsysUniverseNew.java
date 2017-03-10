package com.massivecraft.massivecore.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.Multiverse;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.command.type.store.TypeMultiverse;

public class CmdMassiveCoreUsysUniverseNew extends MassiveCoreCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreUsysUniverseNew()
	{
		// Parameters
		this.addParameter(TypeString.get(), "universe").setDesc("name of universe to create");
		this.addParameter(TypeMultiverse.get(), "multiverse").setDesc("the multiverse of the universe to create");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		String universe = this.readArg();
		Multiverse multiverse = this.readArg();
		
		if (multiverse.containsUniverse(universe))
		{
			msg("<b>The universe <h>%s<b> already exists in multiverse <h>%s<b>.", universe, multiverse.getId());
			return;
		}
		
		multiverse.newUniverse(universe);
		
		msg("<g>Created universe <h>%s<g> in multiverse <h>%s<g>.", universe, multiverse.getId());
	}
	
}
