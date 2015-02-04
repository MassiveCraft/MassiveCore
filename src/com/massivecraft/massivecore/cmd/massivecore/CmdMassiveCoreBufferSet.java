package com.massivecraft.massivecore.cmd.massivecore;

import com.massivecraft.massivecore.MassiveCoreEngineVariable;
import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

public class CmdMassiveCoreBufferSet extends MassiveCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreBufferSet()
	{
		// Aliases
		this.addAliases("s", "set");
		
		// Args
		this.addRequiredArg("string");
		this.setErrorOnToManyArgs(false);
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MassiveCorePerm.BUFFER_SET.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		String string = this.argConcatFrom(0);
		
		MassiveCoreEngineVariable.setBuffer(sender, string);
		
		msg("<i>Buffer was Set");
	}
	
}
