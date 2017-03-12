package com.massivecraft.massivecore.command.massivecore;

import com.massivecraft.massivecore.engine.EngineMassiveCoreVariable;

public class CmdMassiveCoreBufferClear extends MassiveCoreCommand
{
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		EngineMassiveCoreVariable.setBuffer(sender, "");
		
		msg("<i>Buffer Clear");
	}
	
}
