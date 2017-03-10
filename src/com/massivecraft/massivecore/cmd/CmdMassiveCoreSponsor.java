package com.massivecraft.massivecore.cmd;

import com.massivecraft.massivecore.MassiveCoreMConf;
import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.command.MassiveCommandToggle;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;

public class CmdMassiveCoreSponsor extends MassiveCommandToggle
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static CmdMassiveCoreSponsor i = new CmdMassiveCoreSponsor();
	public static CmdMassiveCoreSponsor get() { return i; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreSponsor()
	{
		// Aliases
		this.addAliases("sponsor");

		// Requirements
		this.addRequirements(RequirementHasPerm.get(MassiveCorePerm.SPONSOR));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean getValue()
	{
		return MassiveCoreMConf.get().sponsorEnabled;
	}
	
	@Override
	public void setValue(boolean value)
	{
		MassiveCoreMConf.get().sponsorEnabled = value;
		MassiveCoreMConf.get().changed();
	}
	
}
