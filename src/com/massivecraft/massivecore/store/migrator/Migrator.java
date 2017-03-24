package com.massivecraft.massivecore.store.migrator;

import com.massivecraft.massivecore.xlib.gson.JsonObject;

public interface Migrator
{
	// -------------------------------------------- //
	// MIGRATION
	// -------------------------------------------- //

	void migrate(JsonObject entity);

}
