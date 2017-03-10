package com.massivecraft.massivecore.cmd;

import com.massivecraft.massivecore.MassiveCoreMConf;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.TypeStringCommand;
import com.massivecraft.massivecore.mixin.MixinCommand;

public class CmdMassiveCoreClick extends MassiveCoreCommand
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
		// Parameters
		this.addParameter(null, TypeStringCommand.get(), "command", "none", true).setDesc("the command to perform");
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
		MixinCommand.get().dispatchCommand(sender, command);
	}
	
}
