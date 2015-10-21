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
		// Children
		this.addChild(this.cmdMassiveCoreUsysUniverseNew);
		this.addChild(this.cmdMassiveCoreUsysUniverseDel);
		this.addChild(this.cmdMassiveCoreUsysUniverseClear);
		
		// Aliases
		this.addAliases("universe");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MassiveCorePerm.USYS_UNIVERSE.node));
	}

}
