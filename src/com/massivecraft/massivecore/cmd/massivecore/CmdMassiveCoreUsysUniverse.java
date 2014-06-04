package com.massivecraft.massivecore.cmd.massivecore;

import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

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
		// SubCommands
		this.addSubCommand(this.cmdMassiveCoreUsysUniverseNew);
		this.addSubCommand(this.cmdMassiveCoreUsysUniverseDel);
		this.addSubCommand(this.cmdMassiveCoreUsysUniverseClear);
		
		// Aliases
		this.addAliases("u", "universe");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MassiveCorePerm.USYS_UNIVERSE.node));
	}

}
