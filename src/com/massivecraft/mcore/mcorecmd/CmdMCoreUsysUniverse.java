package com.massivecraft.mcore.mcorecmd;

import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdMCoreUsysUniverse extends MCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdMCoreUsysUniverseNew cmdUsysUniverseNew = new CmdMCoreUsysUniverseNew();
	public CmdMCoreUsysUniverseDel cmdUsysUniverseDel = new CmdMCoreUsysUniverseDel();
	public CmdMCoreUsysUniverseClear cmdUsysUniverseClear = new CmdMCoreUsysUniverseClear();
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMCoreUsysUniverse()
	{
		// SubCommands
		this.addSubCommand(this.cmdUsysUniverseNew);
		this.addSubCommand(this.cmdUsysUniverseDel);
		this.addSubCommand(this.cmdUsysUniverseClear);
		
		// Aliases
		this.addAliases("u", "universe");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_MCORE_USYS_UNIVERSE.node));
	}

}
