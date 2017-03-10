package com.massivecraft.massivecore.cmd;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.Multiverse;
import com.massivecraft.massivecore.command.type.store.TypeMultiverse;

public class CmdMassiveCoreUsysMultiverseDel extends MassiveCoreCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreUsysMultiverseDel()
	{
		// Parameters
		this.addParameter(TypeMultiverse.get(), "multiverse").setDesc("the multiverse to delete");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		Multiverse multiverse = this.readArg();
		
		String id = multiverse.getId();
		
		if (id.equals(MassiveCore.DEFAULT))
		{
			msg("<b>You can't delete the default multiverse.");
			return;
		}
		
		multiverse.detach();
		
		msg("<g>Deleted multiverse <h>%s<g>.", id);
	}
	
}
