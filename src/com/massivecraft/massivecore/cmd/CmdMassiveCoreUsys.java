package com.massivecraft.massivecore.cmd;

import com.massivecraft.massivecore.MassiveCoreMConf;

import java.util.List;

public class CmdMassiveCoreUsys extends MassiveCoreCommand
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static CmdMassiveCoreUsys i = new CmdMassiveCoreUsys() { public List<String> getAliases() { return MassiveCoreMConf.get().aliasesUsys; } };
	public static CmdMassiveCoreUsys get() { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdMassiveCoreUsysMultiverse cmdMassiveCoreUsysMultiverse = new CmdMassiveCoreUsysMultiverse();
	public CmdMassiveCoreUsysUniverse cmdMassiveCoreUsysUniverse = new CmdMassiveCoreUsysUniverse();
	public CmdMassiveCoreUsysWorld cmdMassiveCoreUsysWorld = new CmdMassiveCoreUsysWorld();
	public CmdMassiveCoreUsysAspect cmdMassiveCoreUsysAspect = new CmdMassiveCoreUsysAspect();
	
}
