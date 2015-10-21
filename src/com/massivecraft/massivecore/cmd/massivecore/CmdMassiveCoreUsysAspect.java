package com.massivecraft.massivecore.cmd.massivecore;

import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

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
		this.addAliases("a", "aspect");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MassiveCorePerm.USYS_ASPECT.node));
	}

}
