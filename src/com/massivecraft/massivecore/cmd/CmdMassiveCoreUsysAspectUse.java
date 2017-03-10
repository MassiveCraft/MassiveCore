package com.massivecraft.massivecore.cmd;

import com.massivecraft.massivecore.Aspect;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.Multiverse;
import com.massivecraft.massivecore.command.type.store.TypeAspect;
import com.massivecraft.massivecore.command.type.store.TypeMultiverse;

public class CmdMassiveCoreUsysAspectUse extends MassiveCoreCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreUsysAspectUse()
	{
		// Parameters
		this.addParameter(TypeAspect.get(), "aspect").setDesc("the aspect to modify");
		this.addParameter(TypeMultiverse.get(), "multiverse").setDesc("the multiverse which the aspect should use");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		Aspect aspect = this.readArg();
		Multiverse multiverse = this.readArg();
		
		aspect.setMultiverse(multiverse);
		
		msg("<g>The aspect <h>%s<g> now use multiverse <h>%s<g>.", aspect.getId(), multiverse.getId());
	}
	
}
