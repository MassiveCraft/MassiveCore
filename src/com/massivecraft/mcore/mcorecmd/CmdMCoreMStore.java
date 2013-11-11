package com.massivecraft.mcore.mcorecmd;

import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdMCoreMStore extends MCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdMCoreMStoreStats cmdMCoreMStoreStats = new CmdMCoreMStoreStats();
	public CmdMCoreMStoreListcolls cmdMCoreMStoreListcolls = new CmdMCoreMStoreListcolls();
	public CmdMCoreMStoreCopydb cmdMCoreMStoreCopydb = new CmdMCoreMStoreCopydb();
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMCoreMStore()
	{
		// SubCommands
		this.addSubCommand(this.cmdMCoreMStoreStats);
		this.addSubCommand(this.cmdMCoreMStoreListcolls);
		this.addSubCommand(this.cmdMCoreMStoreCopydb);
		
		// Args
		this.addAliases("mstore");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_MCORE_MSTORE.node));
	}

}
