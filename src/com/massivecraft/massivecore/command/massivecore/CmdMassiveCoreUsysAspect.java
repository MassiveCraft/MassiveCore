package com.massivecraft.massivecore.command.massivecore;

import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;

public class CmdMassiveCoreUsysAspect extends MassiveCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdMassiveCoreUsysAspectList cmdMassiveCoreUsysAspectList = new CmdMassiveCoreUsysAspectList();
	public CmdMassiveCoreUsysAspectShow cmdMassiveCoreUsysAspectShow = new CmdMassiveCoreUsysAspectShow();
	public CmdMassiveCoreUsysAspectUse cmdMassiveCoreUsysAspectUse = new CmdMassiveCoreUsysAspectUse();
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreUsysAspect()
	{
		// Children
		this.addChild(this.cmdMassiveCoreUsysAspectList);
		this.addChild(this.cmdMassiveCoreUsysAspectShow);
		this.addChild(this.cmdMassiveCoreUsysAspectUse);
		
		// Aliases
		this.addAliases("aspect");
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(MassiveCorePerm.USYS_ASPECT.node));
	}

}
