package com.massivecraft.massivecore.cmd.massivecore;

import com.massivecraft.massivecore.MassiveCoreEngineVariable;
import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.arg.ARInteger;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;

public class CmdMassiveCoreBufferWhitespace extends MassiveCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreBufferWhitespace()
	{
		// Aliases
		this.addAliases("w", "whitespace");
		
		// Args
		this.addArg(1, ARInteger.get(), "times").setDesc("the amount of whitespace to add to your buffer");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MassiveCorePerm.BUFFER_WHITESPACE.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		int times = this.readArg();
		
		String string = Txt.repeat(" ", times);
		
		String buffer = MassiveCoreEngineVariable.getBuffer(sender);
		buffer += string;
		MassiveCoreEngineVariable.setBuffer(sender, buffer);
		
		msg("<i>Buffer Whitespace");
	}
	
}
