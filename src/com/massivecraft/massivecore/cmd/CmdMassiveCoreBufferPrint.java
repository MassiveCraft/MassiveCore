package com.massivecraft.massivecore.cmd;

import com.massivecraft.massivecore.engine.EngineMassiveCoreVariable;

public class CmdMassiveCoreBufferPrint extends MassiveCoreCommand
{
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		String buffer = EngineMassiveCoreVariable.getBuffer(sender);
		if (buffer == null || buffer.length() == 0)
		{
			msg("<i>Nothing to print. Your buffer is empty.");
			return;
		}
		
		msg("<i>Printing your buffer on the line below:");
		message(buffer);
	}
	
}
