package com.massivecraft.massivecore.command.massivecore;

import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;

public class CmdMassiveCoreUsysUniverse extends MassiveCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdMassiveCoreUsysUniverseNew cmdMassiveCoreUsysUniverseNew = new CmdMassiveCoreUsysUniverseNew();
	public CmdMassiveCoreUsysUniverseDel cmdMassiveCoreUsysUniverseDel = new CmdMassiveCoreUsysUniverseDel();
	public CmdMassiveCoreUsysUniverseClear cmdMassiveCoreUsysUniverseClear = new CmdMassiveCoreUsysUniverseClear();
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreUsysUniverse()
	{
		// Children
		this.addChild(this.cmdMassiveCoreUsysUniverseNew);
		this.addChild(this.cmdMassiveCoreUsysUniverseDel);
		this.addChild(this.cmdMassiveCoreUsysUniverseClear);
		
		// Aliases
		this.addAliases("universe");
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(MassiveCorePerm.USYS_UNIVERSE.node));
	}

}
