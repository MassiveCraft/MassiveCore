package com.massivecraft.mcore.mcorecmd;

import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdMCoreUsys extends MCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdMCoreUsysMultiverse cmdUsysMultiverse = new CmdMCoreUsysMultiverse();
	public CmdMCoreUsysUniverse cmdUsysUniverse = new CmdMCoreUsysUniverse();
	public CmdMCoreUsysWorld cmdUsysWorld = new CmdMCoreUsysWorld();
	public CmdMCoreUsysAspect cmdUsysAspect = new CmdMCoreUsysAspect();
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMCoreUsys()
	{
		// SubCommands
		this.addSubCommand(this.cmdUsysMultiverse);
		this.addSubCommand(this.cmdUsysUniverse);
		this.addSubCommand(this.cmdUsysWorld);
		this.addSubCommand(this.cmdUsysAspect);
		
		// Aliases
		this.addAliases("usys");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_MCORE_USYS.node));
	}

}
