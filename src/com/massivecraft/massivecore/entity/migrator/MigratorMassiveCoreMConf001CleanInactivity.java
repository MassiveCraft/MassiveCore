package com.massivecraft.massivecore.entity.migrator;

import com.massivecraft.massivecore.MassiveCoreMConf;
import com.massivecraft.massivecore.store.migrator.MigratorFieldRename;
import com.massivecraft.massivecore.store.migrator.MigratorRoot;

public class MigratorMassiveCoreMConf001CleanInactivity extends MigratorRoot
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MigratorMassiveCoreMConf001CleanInactivity i = new MigratorMassiveCoreMConf001CleanInactivity();
	public static MigratorMassiveCoreMConf001CleanInactivity get() { return i; }
	private MigratorMassiveCoreMConf001CleanInactivity()
	{
		super(MassiveCoreMConf.class);
		this.addInnerMigrator(MigratorFieldRename.get("playercleanPeriodMillis", "cleanTaskPeriodMillis"));
		this.addInnerMigrator(MigratorFieldRename.get("playercleanOffsetMillis", "cleanTaskOffsetMillis"));
		this.addInnerMigrator(MigratorFieldRename.get("playercleanLastMillis", "cleanTaskLastMillis"));
	}
	
}
