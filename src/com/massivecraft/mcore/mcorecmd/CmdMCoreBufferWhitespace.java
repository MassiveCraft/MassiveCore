package com.massivecraft.mcore.mcorecmd;

import com.massivecraft.mcore.EngineMCoreVariable;
import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.arg.ARInteger;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.util.Txt;

public class CmdMCoreBufferWhitespace extends MCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMCoreBufferWhitespace()
	{
		// Aliases
		this.addAliases("w", "whitespace");
		
		// Args
		this.addOptionalArg("times", "1");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MCorePerm.BUFFER_WHITESPACE.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		Integer times = this.arg(0, ARInteger.get(), 1);
		if (times == null) return;
		
		String string = Txt.repeat(" ", times);
		
		String buffer = EngineMCoreVariable.getBuffer(sender);
		buffer += string;
		EngineMCoreVariable.setBuffer(sender, buffer);
		
		msg("<i>Buffer Whitespace");
	}
	
}
