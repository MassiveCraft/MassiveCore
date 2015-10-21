package com.massivecraft.massivecore.cmd.massivecore;

import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

public class CmdMassiveCoreBuffer extends MassiveCommand
{
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
		this.addRequirements(ReqHasPerm.get(MassiveCorePerm.BUFFER.node));
	}

}
