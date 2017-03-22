package com.massivecraft.massivecore.store.migration;

import com.massivecraft.massivecore.ActivePriority;
import com.massivecraft.massivecore.xlib.gson.JsonObject;

@ActivePriority(ActivePriority.PRIORITY_MIGRATOR)
public interface VersionMigrator
{
	// -------------------------------------------- //
	// MIGRATION
	// -------------------------------------------- //

	public void migrate(JsonObject entity);

}
