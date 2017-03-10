package com.massivecraft.massivecore.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.engine.EngineMassiveCoreVariable;

public class CmdMassiveCoreBufferSet extends MassiveCoreCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreBufferSet()
	{
		// Parameters
		this.addParameter(TypeString.get(), "text", true).setDesc("the text to set your buffer to");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		String string = this.readArg();
		
		EngineMassiveCoreVariable.setBuffer(sender, string);
		
		msg("<i>Buffer was Set");
	}
	
}
