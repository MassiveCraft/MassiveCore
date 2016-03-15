package com.massivecraft.massivecore.command.massivecore;

import com.massivecraft.massivecore.MassiveCoreMConf;
import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.TypeStringCommand;
import com.massivecraft.massivecore.mixin.Mixin;

public class CmdMassiveCoreClick extends MassiveCommand
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static CmdMassiveCoreClick i = new CmdMassiveCoreClick();
	public static CmdMassiveCoreClick get() { return i; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreClick()
	{
		// Aliases
		this.addAliases("click");
		
		// Parameters
		this.addParameter(null, TypeStringCommand.get(), "command", "none", true).setDesc("the command to perform");
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(MassiveCorePerm.CLICK.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		MassiveCoreMConf.get().clickSound.run(me);
		String command = this.readArg();
		if (command == null) return;
		Mixin.dispatchCommand(sender, command);
	}
	
}
