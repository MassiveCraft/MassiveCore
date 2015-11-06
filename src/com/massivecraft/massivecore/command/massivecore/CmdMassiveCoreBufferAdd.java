package com.massivecraft.massivecore.command.massivecore;

import com.massivecraft.massivecore.MassiveCoreEngineVariable;
import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

public class CmdMassiveCoreBufferAdd extends MassiveCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreBufferAdd()
	{
		// Aliases
		this.addAliases("add");
		
		// Parameters
		this.addParameter(TypeString.get(), "text", true).setDesc("the text to add to your buffer");
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(MassiveCorePerm.BUFFER_ADD.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		String string = this.readArg();
		
		String buffer = MassiveCoreEngineVariable.getBuffer(sender);
		buffer += string;
		MassiveCoreEngineVariable.setBuffer(sender, buffer);
		
		msg("<i>Buffer Add");
	}
	
}
