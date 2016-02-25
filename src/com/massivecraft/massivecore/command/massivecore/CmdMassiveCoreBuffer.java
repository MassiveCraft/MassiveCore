package com.massivecraft.massivecore.command.massivecore;

import java.util.List;

import com.massivecraft.massivecore.MassiveCoreMConf;
import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;

public class CmdMassiveCoreBuffer extends MassiveCommand
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static CmdMassiveCoreBuffer i = new CmdMassiveCoreBuffer() { public List<String> getAliases() { return MassiveCoreMConf.get().aliasesOuterMassiveCoreBuffer; } };
	public static CmdMassiveCoreBuffer get() { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdMassiveCoreBufferPrint cmdMassiveCoreBufferPrint = new CmdMassiveCoreBufferPrint();
	public CmdMassiveCoreBufferClear cmdMassiveCoreBufferClear = new CmdMassiveCoreBufferClear();
	public CmdMassiveCoreBufferSet cmdMassiveCoreBufferSet = new CmdMassiveCoreBufferSet();
	public CmdMassiveCoreBufferAdd cmdMassiveCoreBufferAdd = new CmdMassiveCoreBufferAdd();
	public CmdMassiveCoreBufferWhitespace cmdMassiveCoreBufferWhitespace = new CmdMassiveCoreBufferWhitespace();
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreBuffer()
	{
		// Children
		this.addChild(this.cmdMassiveCoreBufferPrint);
		this.addChild(this.cmdMassiveCoreBufferClear);
		this.addChild(this.cmdMassiveCoreBufferSet);
		this.addChild(this.cmdMassiveCoreBufferAdd);
		this.addChild(this.cmdMassiveCoreBufferWhitespace);
		
		// Aliases
		this.addAliases("buffer");
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(MassiveCorePerm.BUFFER.node));
	}

}
