package com.massivecraft.massivecore.cmd.massivecore;

import com.massivecraft.massivecore.MassiveCoreEngineVariable;
import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.arg.ARString;
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
		this.addArg(ARString.get(), "text", true).setDesc("the text to add to your buffer");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MassiveCorePerm.BUFFER_ADD.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		String string = (String) this.readArg();
		
		String buffer = MassiveCoreEngineVariable.getBuffer(sender);
		buffer += string;
		MassiveCoreEngineVariable.setBuffer(sender, buffer);
		
		msg("<i>Buffer Add");
	}
	
}
