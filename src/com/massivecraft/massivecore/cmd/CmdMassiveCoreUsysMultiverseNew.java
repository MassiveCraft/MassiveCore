package com.massivecraft.massivecore.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.MultiverseColl;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

public class CmdMassiveCoreUsysMultiverseNew extends MassiveCoreCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreUsysMultiverseNew()
	{
		// Parameters
		this.addParameter(TypeString.get(), "multiverse").setDesc("name of multiverse to create");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		String id = this.readArg();
		
		if (MultiverseColl.get().containsId(id))
		{
			msg("<b>The multiverse <h>%s<b> already exists.", id);
			return;
		}
		
		MultiverseColl.get().create(id);
		
		msg("<g>Created multiverse <h>%s<g>.", id);
	}
	
}
