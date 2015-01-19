package com.massivecraft.massivecore.store;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.massivecraft.massivecore.xlib.gson.JsonElement;

public interface Driver
{
	// Returns the name of the driver.
	public String getName();
	
	// Get a database instance from the driver
	public Db getDb(String uri);
	
	// What collections are in the database?		
	public Set<String> getCollnames(Db db);
	
	// Rename a collection
	public boolean renameColl(Db db, String from, String to);
	
	// Is id X in the collection?
	public boolean containsId(Coll<?> coll, String id);
	
	// When was X last altered?
	public Long getMtime(Coll<?> coll, String id);
		
	// What ids are in the collection?
	public Collection<String> getIds(Coll<?> coll);
	
	// Return a map of all ids with their corresponding mtimes
	public Map<String, Long> getId2mtime(Coll<?> coll);
	
	// Load the raw data for X. The second part of the entry is the remote mtime at the load.
	public Entry<JsonElement, Long> load(Coll<?> coll, String id);
	
	// Load all database content at once
	public Map<String, Entry<JsonElement, Long>> loadAll(Coll<?> coll);
	
	// Save raw data as X
	// Return value is the new mtime (we caused the change).
	// If the mtime is null something failed.
	public Long save(Coll<?> coll, String id, JsonElement data);
	
	// Delete X
	public void delete(Coll<?> coll, String id);
}
