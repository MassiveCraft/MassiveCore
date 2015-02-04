package com.massivecraft.massivecore.cmd.massivecore;

import com.massivecraft.massivecore.MassiveCoreEngineVariable;
import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

public class CmdMassiveCoreBufferAdd extends MassiveCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreBufferAdd()
	{
		// Aliases
		this.addAliases("a", "add");
		
		// Args
		this.addRequiredArg("string");
		this.setErrorOnToManyArgs(false);
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MassiveCorePerm.BUFFER_ADD.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		String string = this.argConcatFrom(0);
		
		String buffer = MassiveCoreEngineVariable.getBuffer(sender);
		buffer += string;
		MassiveCoreEngineVariable.setBuffer(sender, buffer);
		
		msg("<i>Buffer Add");
	}
	
}
