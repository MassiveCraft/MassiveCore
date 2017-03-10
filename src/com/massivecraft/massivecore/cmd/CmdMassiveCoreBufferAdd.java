package com.massivecraft.massivecore.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.engine.EngineMassiveCoreVariable;

public class CmdMassiveCoreBufferAdd extends MassiveCoreCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreBufferAdd()
	{
		// Parameters
		this.addParameter(TypeString.get(), "text", true).setDesc("the text to add to your buffer");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		String string = this.readArg();
		
		String buffer = EngineMassiveCoreVariable.getBuffer(sender);
		buffer += string;
		EngineMassiveCoreVariable.setBuffer(sender, buffer);
		
		msg("<i>Buffer Add");
	}
	
}
