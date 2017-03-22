package com.massivecraft.massivecore.cmd;

import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;

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
		// Children
		this.addChild(this.cmdMassiveCoreUsysMultiverseList);
		this.addChild(this.cmdMassiveCoreUsysMultiverseShow);
		this.addChild(this.cmdMassiveCoreUsysMultiverseNew);
		this.addChild(this.cmdMassiveCoreUsysMultiverseDel);
		
		// Aliases
		this.addAliases("multiverse");
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(MassiveCorePerm.USYS_MULTIVERSE));
	}

}
