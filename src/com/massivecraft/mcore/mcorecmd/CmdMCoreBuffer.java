package com.massivecraft.mcore.mcorecmd;

import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdMCoreBuffer extends MCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdMCoreBufferPrint cmdMCoreBufferPrint = new CmdMCoreBufferPrint();
	public CmdMCoreBufferClear cmdMCoreBufferClear = new CmdMCoreBufferClear();
	public CmdMCoreBufferSet cmdMCoreBufferSet = new CmdMCoreBufferSet();
	public CmdMCoreBufferAdd cmdMCoreBufferAdd = new CmdMCoreBufferAdd();
	public CmdMCoreBufferWhitespace cmdMCoreBufferWhitespace = new CmdMCoreBufferWhitespace();
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMCoreBuffer()
	{
		// SubCommands
		this.addSubCommand(this.cmdMCoreBufferPrint);
		this.addSubCommand(this.cmdMCoreBufferClear);
		this.addSubCommand(this.cmdMCoreBufferSet);
		this.addSubCommand(this.cmdMCoreBufferAdd);
		this.addSubCommand(this.cmdMCoreBufferWhitespace);
		
		// Aliases
		this.addAliases("buffer");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MCorePerm.BUFFER.node));
	}

}
