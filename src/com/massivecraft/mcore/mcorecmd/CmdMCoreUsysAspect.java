package com.massivecraft.mcore.mcorecmd;

import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdMCoreUsysAspect extends MCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdMCoreUsysAspectList cmdUsysAspectList = new CmdMCoreUsysAspectList();
	public CmdMCoreUsysAspectShow cmdUsysAspectShow = new CmdMCoreUsysAspectShow();
	public CmdMCoreUsysAspectUse cmdUsysAspectUse = new CmdMCoreUsysAspectUse();
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMCoreUsysAspect()
	{
		// SubCommands
		this.addSubCommand(this.cmdUsysAspectList);
		this.addSubCommand(this.cmdUsysAspectShow);
		this.addSubCommand(this.cmdUsysAspectUse);
		
		// Aliases
		this.addAliases("a", "aspect");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_MCORE_USYS_ASPECT.node));
	}

}
