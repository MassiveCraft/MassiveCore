package com.massivecraft.massivecore.cmd.massivecore;

import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

public class CmdMassiveCoreStore extends MassiveCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdMassiveCoreStoreStats cmdMassiveCoreStoreStats = new CmdMassiveCoreStoreStats();
	public CmdMassiveCoreStoreListcolls cmdMassiveCoreStoreListcolls = new CmdMassiveCoreStoreListcolls();
	public CmdMassiveCoreStoreCopydb cmdMassiveCoreStoreCopydb = new CmdMassiveCoreStoreCopydb();
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreStore()
	{
		// SubCommands
		this.addSubCommand(this.cmdMassiveCoreStoreStats);
		this.addSubCommand(this.cmdMassiveCoreStoreListcolls);
		this.addSubCommand(this.cmdMassiveCoreStoreCopydb);
		
		// Args
		this.addAliases("store");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MassiveCorePerm.STORE.node));
	}

}
