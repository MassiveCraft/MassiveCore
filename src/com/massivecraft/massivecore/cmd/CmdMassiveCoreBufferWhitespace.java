package com.massivecraft.massivecore.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.engine.EngineMassiveCoreVariable;
import com.massivecraft.massivecore.util.Txt;

public class CmdMassiveCoreBufferWhitespace extends MassiveCoreCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreBufferWhitespace()
	{
		// Parameters
		this.addParameter(1, TypeInteger.get(), "times").setDesc("the amount of whitespace to add to your buffer");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		int times = this.readArg();
		
		String string = Txt.repeat(" ", times);
		
		String buffer = EngineMassiveCoreVariable.getBuffer(sender);
		buffer += string;
		EngineMassiveCoreVariable.setBuffer(sender, buffer);
		
		msg("<i>Buffer Whitespace");
	}
	
}
