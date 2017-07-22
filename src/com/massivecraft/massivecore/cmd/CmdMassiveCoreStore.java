package com.massivecraft.massivecore.cmd;

import com.massivecraft.massivecore.MassiveCoreMConf;

import java.util.List;

public class CmdMassiveCoreStore extends MassiveCoreCommand
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static CmdMassiveCoreStore i = new CmdMassiveCoreStore() { public List<String> getAliases() { return MassiveCoreMConf.get().aliasesMstore; } };
	public static CmdMassiveCoreStore get() { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdMassiveCoreStoreStats cmdMassiveCoreStoreStats = new CmdMassiveCoreStoreStats();
	public CmdMassiveCoreStoreListcolls cmdMassiveCoreStoreListcolls = new CmdMassiveCoreStoreListcolls();
	public CmdMassiveCoreStoreCopydb cmdMassiveCoreStoreCopydb = new CmdMassiveCoreStoreCopydb();
	public CmdMassiveCoreStoreClean cmdMassiveCoreStoreClean = new CmdMassiveCoreStoreClean();
	
}
