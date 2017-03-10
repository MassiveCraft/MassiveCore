package com.massivecraft.massivecore.cmd;

import com.massivecraft.massivecore.MassiveCoreMConf;

import java.util.List;

public class CmdMassiveCoreBuffer extends MassiveCoreCommand
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static CmdMassiveCoreBuffer i = new CmdMassiveCoreBuffer() { public List<String> getAliases() { return MassiveCoreMConf.get().aliasesBuffer; } };
	public static CmdMassiveCoreBuffer get() { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdMassiveCoreBufferPrint cmdMassiveCoreBufferPrint = new CmdMassiveCoreBufferPrint();
	public CmdMassiveCoreBufferClear cmdMassiveCoreBufferClear = new CmdMassiveCoreBufferClear();
	public CmdMassiveCoreBufferSet cmdMassiveCoreBufferSet = new CmdMassiveCoreBufferSet();
	public CmdMassiveCoreBufferAdd cmdMassiveCoreBufferAdd = new CmdMassiveCoreBufferAdd();
	public CmdMassiveCoreBufferWhitespace cmdMassiveCoreBufferWhitespace = new CmdMassiveCoreBufferWhitespace();
	
}
