package com.massivecraft.massivecore.cmd.massivecore;

import com.massivecraft.massivecore.Aspect;
import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.Multiverse;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.type.TypeAspect;
import com.massivecraft.massivecore.cmd.type.TypeMultiverse;

public class CmdMassiveCoreUsysAspectUse extends MassiveCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreUsysAspectUse()
	{
		// Aliases
		this.addAliases("u", "use");
		
		// Parameters
		this.addParameter(TypeAspect.get(), "aspect").setDesc("the aspect to modify");
		this.addParameter(TypeMultiverse.get(), "multiverse").setDesc("the multiverse which the aspect should use");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MassiveCorePerm.USYS_ASPECT_USE.node));
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
