package com.massivecraft.massivecore.command.massivecore;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.VersionCommand;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;

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
		// Children
		this.addChild(this.cmdMassiveCoreUsys);
		this.addChild(this.cmdMassiveCoreMStore);
		this.addChild(this.cmdMassiveCoreId);
		this.addChild(this.cmdMassiveCoreTest);
		this.addChild(this.cmdMassiveCoreHearsound);
		this.addChild(this.cmdMassiveCoreBuffer);
		this.addChild(this.cmdMassiveCoreCmdurl);
		this.addChild(this.cmdMassiveCoreVersion);
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(MassiveCorePerm.BASECOMMAND.node));
	}

}
