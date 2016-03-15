package com.massivecraft.massivecore.command.massivecore;

import java.util.List;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveCoreMConf;
import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.MassiveCommandVersion;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;

public class CmdMassiveCore extends MassiveCommand
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static CmdMassiveCore i = new CmdMassiveCore();
	public static CmdMassiveCore get() { return i; }
	
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
	public CmdMassiveCoreClick cmdMassiveCoreClick = new CmdMassiveCoreClick();
	public MassiveCommandVersion cmdMassiveCoreVersion = new MassiveCommandVersion(MassiveCore.get(), MassiveCorePerm.VERSION.node, "v", "version");
	
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
		this.addChild(this.cmdMassiveCoreClick);
		this.addChild(this.cmdMassiveCoreVersion);
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(MassiveCorePerm.BASECOMMAND.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public List<String> getAliases()
	{
		return MassiveCoreMConf.get().aliasesOuterMassiveCore;
	}

}
