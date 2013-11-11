package com.massivecraft.mcore.mcorecmd;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.VersionCommand;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdMCore extends MCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdMCoreUsys cmdMCoreUsys = new CmdMCoreUsys();
	public CmdMCoreMStore cmdMCoreMStore = new CmdMCoreMStore();
	public CmdMCoreId cmdMCoreId = new CmdMCoreId();
	public CmdMCoreHearsound cmdMCoreHearsound = new CmdMCoreHearsound();
	public VersionCommand cmdMCoreVersion = new VersionCommand(MCore.get(), MCorePerm.CMD_MCORE_VERSION.node, "v", "version");
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMCore()
	{
		// SubCommands
		this.addSubCommand(this.cmdMCoreUsys);
		this.addSubCommand(this.cmdMCoreMStore);
		this.addSubCommand(this.cmdMCoreId);
		this.addSubCommand(this.cmdMCoreHearsound);
		this.addSubCommand(this.cmdMCoreVersion);
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_MCORE.node));
	}

}
