package com.massivecraft.massivecore.cmd.massivecore;

import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

public class CmdMassiveCoreUsys extends MassiveCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdMassiveCoreUsysMultiverse cmdMassiveCoreUsysMultiverse = new CmdMassiveCoreUsysMultiverse();
	public CmdMassiveCoreUsysUniverse cmdMassiveCoreUsysUniverse = new CmdMassiveCoreUsysUniverse();
	public CmdMassiveCoreUsysWorld cmdMassiveCoreUsysWorld = new CmdMassiveCoreUsysWorld();
	public CmdMassiveCoreUsysAspect cmdMassiveCoreUsysAspect = new CmdMassiveCoreUsysAspect();
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreUsys()
	{
		// Children
		this.addChild(this.cmdMassiveCoreUsysMultiverse);
		this.addChild(this.cmdMassiveCoreUsysUniverse);
		this.addChild(this.cmdMassiveCoreUsysWorld);
		this.addChild(this.cmdMassiveCoreUsysAspect);
		
		// Aliases
		this.addAliases("usys");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MassiveCorePerm.USYS.node));
	}

}
