package com.massivecraft.massivecore.cmd;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveCoreMConf;
import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.command.MassiveCommandVersion;
import com.massivecraft.massivecore.command.editor.CommandEditAbstract;
import com.massivecraft.massivecore.command.editor.CommandEditSingleton;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;

import java.util.List;

public class CmdMassiveCore extends MassiveCoreCommand
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
	public CommandEditAbstract<MassiveCoreMConf, MassiveCoreMConf> cmdMassiveCoreConfig = new CommandEditSingleton<>(MassiveCoreMConf.get()).addRequirements(RequirementHasPerm.get(MassiveCorePerm.CONFIG));
	public CmdMassiveCoreSponsor cmdMassiveCoreSponsor = new CmdMassiveCoreSponsor();
	public CmdMassiveCoreClick cmdMassiveCoreClick = new CmdMassiveCoreClick();
	public MassiveCommandVersion cmdMassiveCoreVersion = new MassiveCommandVersion(MassiveCore.get()).setAliases("v", "version").addRequirements(RequirementHasPerm.get(MassiveCorePerm.VERSION));
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public List<String> getAliases()
	{
		return MassiveCoreMConf.get().aliasesMcore;
	}

}
