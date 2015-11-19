package com.massivecraft.massivecore.command.massivecore;

import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.engine.EngineMassiveCoreVariable;

public class CmdMassiveCoreBufferSet extends MassiveCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreBufferSet()
	{
		// Aliases
		this.addAliases("set");
		
		// Parameters
		this.addParameter(TypeString.get(), "text", true).setDesc("the text to set your buffer to");
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(MassiveCorePerm.BUFFER_SET.node));
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
