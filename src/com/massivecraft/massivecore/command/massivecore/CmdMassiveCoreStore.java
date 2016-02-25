package com.massivecraft.massivecore.command.massivecore;

import java.util.List;

import com.massivecraft.massivecore.MassiveCoreMConf;
import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;

public class CmdMassiveCoreStore extends MassiveCommand
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static CmdMassiveCoreStore i = new CmdMassiveCoreStore() { public List<String> getAliases() { return MassiveCoreMConf.get().aliasesOuterMassiveCoreStore; } };
	public static CmdMassiveCoreStore get() { return i; }
	
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
		// Children
		this.addChild(this.cmdMassiveCoreStoreStats);
		this.addChild(this.cmdMassiveCoreStoreListcolls);
		this.addChild(this.cmdMassiveCoreStoreCopydb);
		
		// Parameters
		this.addAliases("store");
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(MassiveCorePerm.STORE.node));
	}

}
