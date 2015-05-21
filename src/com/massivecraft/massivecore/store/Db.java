package com.massivecraft.massivecore.store;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.massivecraft.massivecore.xlib.gson.JsonElement;

public interface Db
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// Returns the name of the database.
	public String getDbName();
	
	// Returns the driver running this database.
	public Driver getDriver();
	
	// Creates a new collection driver object.
	// This object will be stored inside the Coll.
	public Object createCollDriverObject(Coll<?> coll);
	
	// -------------------------------------------- //
	// DRIVER
	// -------------------------------------------- //
	
	public String getDriverName();
	public Db getDb(String uri); // TODO: This seems a bit odd.
	public boolean dropDb();
	public Set<String> getCollnames();
	public boolean renameColl(String from, String to);
	public boolean containsId(Coll<?> coll, String id);
	public long getMtime(Coll<?> coll, String id);
	public Collection<String> getIds(Coll<?> coll);
	public Map<String, Long> getId2mtime(Coll<?> coll);
	public Entry<JsonElement, Long> load(Coll<?> coll, String id);
	public Map<String, Entry<JsonElement, Long>> loadAll(Coll<?> coll);
	public long save(Coll<?> coll, String id, JsonElement data);
	public void delete(Coll<?> coll, String id);
	public boolean supportsPusher();
	public PusherColl getPusher(Coll<?> coll);
	
}
