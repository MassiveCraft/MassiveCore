package com.massivecraft.massivecore.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.Multiverse;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.command.type.store.TypeMultiverse;

public class CmdMassiveCoreUsysWorld extends MassiveCoreCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreUsysWorld()
	{
		// Parameters
		this.addParameter(TypeString.get(), "world").setDesc("the world to move");
		this.addParameter(TypeString.get(), "universe").setDesc("the universe to move the world ro");
		this.addParameter(TypeMultiverse.get(), "multiverse").setDesc("the multiverse of the universe to move the world to");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		String worldName = this.readArg();
		String universe = this.readArg();
		Multiverse multiverse = (Multiverse) this.readArg();
		
		if (!multiverse.containsUniverse(universe))
		{
			msg("<b>No universe <h>%s<b> exists in multiverse <h>%s<b>.", universe, multiverse.getId());
			return;
		}
		
		String universeOld = multiverse.getUniverseForWorldName(worldName);
		
		if (multiverse.setWorldUniverse(worldName, universe))
		{
			msg("<g>World <h>%s <g>moved from <h>%s <g>to <h>%s <g>universe in multiverse <h>%s<g>.", worldName, universeOld, universe, multiverse.getId());
		}
		else
		{
			msg("<i>World <h>%s <i>is already in universe <h>%s <i>in multiverse <h>%s<i>.", worldName, universe, multiverse.getId());
		}
	}
	
}
