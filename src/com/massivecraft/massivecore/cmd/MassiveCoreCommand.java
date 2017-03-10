package com.massivecraft.massivecore.cmd;

import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.command.MassiveCommand;

public class MassiveCoreCommand extends MassiveCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public MassiveCoreCommand()
	{
		this.setSetupEnabled(true);
		this.setSetupPermClass(MassiveCorePerm.class);

		// The commands are not placed in their ususal place so it can't be found automatically.
		this.setSetupPermBaseClassName(CmdMassiveCore.class.getSimpleName());
	}

}
