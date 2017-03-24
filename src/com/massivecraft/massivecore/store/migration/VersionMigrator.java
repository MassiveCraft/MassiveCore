package com.massivecraft.massivecore.store.migration;

import com.massivecraft.massivecore.xlib.gson.JsonObject;

public interface VersionMigrator
{
	// -------------------------------------------- //
	// MIGRATION
	// -------------------------------------------- //

	void migrate(JsonObject entity);

}
