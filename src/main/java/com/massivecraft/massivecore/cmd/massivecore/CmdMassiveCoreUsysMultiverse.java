package com.massivecraft.massivecore.cmd.massivecore;

import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

public class CmdMassiveCoreUsysMultiverse extends MassiveCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdMassiveCoreUsysMultiverseList cmdMassiveCoreUsysMultiverseList = new CmdMassiveCoreUsysMultiverseList();
	public CmdMassiveCoreUsysMultiverseShow cmdMassiveCoreUsysMultiverseShow = new CmdMassiveCoreUsysMultiverseShow();
	public CmdMassiveCoreUsysMultiverseNew cmdMassiveCoreUsysMultiverseNew = new CmdMassiveCoreUsysMultiverseNew();
	public CmdMassiveCoreUsysMultiverseDel cmdMassiveCoreUsysMultiverseDel = new CmdMassiveCoreUsysMultiverseDel();
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreUsysMultiverse()
	{
		// SubCommands
		this.addSubCommand(this.cmdMassiveCoreUsysMultiverseList);
		this.addSubCommand(this.cmdMassiveCoreUsysMultiverseShow);
		this.addSubCommand(this.cmdMassiveCoreUsysMultiverseNew);
		this.addSubCommand(this.cmdMassiveCoreUsysMultiverseDel);
		
		// Aliases
		this.addAliases("m", "multiverse");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MassiveCorePerm.USYS_MULTIVERSE.node));
	}

}
