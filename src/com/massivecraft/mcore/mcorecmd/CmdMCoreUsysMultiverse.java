package com.massivecraft.mcore.mcorecmd;

import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdMCoreUsysMultiverse extends MCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdMCoreUsysMultiverseList cmdUsysMultiverseList = new CmdMCoreUsysMultiverseList();
	public CmdMCoreUsysMultiverseShow cmdUsysMultiverseShow = new CmdMCoreUsysMultiverseShow();
	public CmdMCoreUsysMultiverseNew cmdUsysMultiverseNew = new CmdMCoreUsysMultiverseNew();
	public CmdMCoreUsysMultiverseDel cmdUsysMultiverseDel = new CmdMCoreUsysMultiverseDel();
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMCoreUsysMultiverse()
	{
		// SubCommands
		this.addSubCommand(this.cmdUsysMultiverseList);
		this.addSubCommand(this.cmdUsysMultiverseShow);
		this.addSubCommand(this.cmdUsysMultiverseNew);
		this.addSubCommand(this.cmdUsysMultiverseDel);
		
		// Aliases
		this.addAliases("m", "multiverse");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_MCORE_USYS_MULTIVERSE.node));
	}

}
