package com.massivecraft.massivecore.cmd.massivecore;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.VersionCommand;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

public class CmdMassiveCore extends MassiveCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdMassiveCoreUsys cmdMassiveCoreUsys = new CmdMassiveCoreUsys();
	public CmdMassiveCoreStore cmdMassiveCoreMStore = new CmdMassiveCoreStore();
	public CmdMassiveCoreId cmdMassiveCoreId = new CmdMassiveCoreId();
	public CmdMassiveCoreTest cmdMassiveCoreTest = new CmdMassiveCoreTest();
	public CmdMassiveCoreHearsound cmdMassiveCoreHearsound = new CmdMassiveCoreHearsound();
	public CmdMassiveCoreBuffer cmdMassiveCoreBuffer = new CmdMassiveCoreBuffer();
	public CmdMassiveCoreCmdurl cmdMassiveCoreCmdurl = new CmdMassiveCoreCmdurl();
	public VersionCommand cmdMassiveCoreVersion = new VersionCommand(MassiveCore.get(), MassiveCorePerm.VERSION.node, "v", "version");
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCore()
	{
		// SubCommands
		this.addSubCommand(this.cmdMassiveCoreUsys);
		this.addSubCommand(this.cmdMassiveCoreMStore);
		this.addSubCommand(this.cmdMassiveCoreId);
		this.addSubCommand(this.cmdMassiveCoreTest);
		this.addSubCommand(this.cmdMassiveCoreHearsound);
		this.addSubCommand(this.cmdMassiveCoreBuffer);
		this.addSubCommand(this.cmdMassiveCoreCmdurl);
		this.addSubCommand(this.cmdMassiveCoreVersion);
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MassiveCorePerm.BASECOMMAND.node));
	}

}
