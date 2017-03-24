package com.massivecraft.massivecore.store;

import com.massivecraft.massivecore.xlib.gson.JsonObject;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public interface Db
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// Returns the name of the database.
	String getDbName();
	
	// Returns the driver running this database.
	Driver getDriver();
	
	// Creates a new collection driver object.
	// This object will be stored inside the Coll.
	Object createCollDriverObject(Coll<?> coll);
	
	// -------------------------------------------- //
	// DRIVER
	// -------------------------------------------- //
	
	String getDriverName();
	Db getDb(String uri); // TODO: This seems a bit odd.
	boolean dropDb();
	Set<String> getCollnames();
	boolean renameColl(String from, String to);
	boolean containsId(Coll<?> coll, String id);
	long getMtime(Coll<?> coll, String id);
	Collection<String> getIds(Coll<?> coll);
	Map<String, Long> getId2mtime(Coll<?> coll);
	Entry<JsonObject, Long> load(Coll<?> coll, String id);
	Map<String, Entry<JsonObject, Long>> loadAll(Coll<?> coll);
	long save(Coll<?> coll, String id, JsonObject data);
	void delete(Coll<?> coll, String id);
	boolean supportsPusher();
	PusherColl getPusher(Coll<?> coll);
	
}
