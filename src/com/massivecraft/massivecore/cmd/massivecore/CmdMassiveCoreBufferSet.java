package com.massivecraft.massivecore.cmd.massivecore;

import com.massivecraft.massivecore.MassiveCoreEngineVariable;
import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.type.TypeString;

public class CmdMassiveCoreBufferSet extends MassiveCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreBufferSet()
	{
		// Aliases
		this.addAliases("s", "set");
		
		// Parameters
		this.addParameter(TypeString.get(), "text", true).setDesc("the text to set your buffer to");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MassiveCorePerm.BUFFER_SET.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		String string = this.readArg();
		
		MassiveCoreEngineVariable.setBuffer(sender, string);
		
		msg("<i>Buffer was Set");
	}
	
}
