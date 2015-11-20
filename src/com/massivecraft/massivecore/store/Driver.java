package com.massivecraft.massivecore.store;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.massivecraft.massivecore.xlib.gson.JsonObject;

public interface Driver
{
	// Returns the name of the driver.
	public String getDriverName();
	
	// Get a database instance from the driver
	public Db getDb(String uri);
	
	// This will delete the whole database and all collections therein.
	public boolean dropDb(Db db);
	
	// What collections are in the database?
	public Set<String> getCollnames(Db db);
	
	// Rename a collection
	public boolean renameColl(Db db, String from, String to);
	
	// Is id X in the collection?
	public boolean containsId(Coll<?> coll, String id);
	
	// When was X last altered?
	// return == null will never happen.
	// return != 0 means X exists and return is when it last was altered.
	// return == 0 means X does not exist in the database.
	public long getMtime(Coll<?> coll, String id);
		
	// What ids are in the collection?
	public Collection<String> getIds(Coll<?> coll);
	
	// Return a map of all ids with their corresponding mtimes
	public Map<String, Long> getId2mtime(Coll<?> coll);
	
	// Load the raw data for X. The second part of the entry is the remote mtime at the load.
	// return == null will never happen.
	// return.getKey() == null || return.getValue() == 0 means something failed.
	public Entry<JsonObject, Long> load(Coll<?> coll, String id);
	
	// Load all database content at once
	// NOTE: This method is assumed to be based on the one above.
	// NOTE: Values where JsonObject == null and Long == 0 may occur.
	public Map<String, Entry<JsonObject, Long>> loadAll(Coll<?> coll);
	
	// Save raw data as X
	// Return value is the new mtime (we caused the change).
	// return == null will never happen.
	// return == 0 means something failed. Usually failures are not catched, though. System.currentTimeMillis() is returned most of the time.
	public long save(Coll<?> coll, String id, JsonObject data);
	
	// Delete X
	public void delete(Coll<?> coll, String id);
	
	// Database pusher
	public boolean supportsPusher();
	public PusherColl getPusher(Coll<?> coll);
}
