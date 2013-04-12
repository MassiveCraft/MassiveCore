package com.massivecraft.mcore.store;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.massivecraft.mcore.store.idstrategy.IdStrategy;
import com.massivecraft.mcore.store.storeadapter.StoreAdapter;

public interface Driver<R>
{
	// Returns the name of the driver.
	public String getName();
	
	// This is the rawdata format this driver works with.
	// Could for example be JsonElement or DBObject
	public Class<R> getRawdataClass();
	
	// Comparison of raw data should be done through this method
	public boolean equal(Object rawOne, Object rawTwo);
	
	// This is some sort of database specific id strategy with built in adapter
	public boolean registerIdStrategy(IdStrategy idStrategy);
	public IdStrategy getIdStrategy(String idStrategyName);
	
	// Get the default store adapter for the driver.
	public StoreAdapter getStoreAdapter();
	
	// Get a database instance from the driver
	public Db<R> getDb(String uri);
	
	// What collections are in the database?		
	public Set<String> getCollnames(Db<?> db);
	
	// Is id X in the collection?
	public boolean containsId(Coll<?> coll, String id);
	
	// When was X last altered?
	public Long getMtime(Coll<?> coll, String id);
		
	// What ids are in the collection?
	public Collection<String> getIds(Coll<?> coll);
	
	// Return a map of all ids with their corresponding mtimes
	public Map<String, Long> getId2mtime(Coll<?> coll);
	
	// Load the raw data for X. The second part of the entry is the remote mtime at the load.
	public Entry<R, Long> load(Coll<?> coll, String id);
	
	// Save raw data as X
	// Return value is the new mtime (we caused the change).
	// If the mtime is null something failed.
	public Long save(Coll<?> coll, String id, final Object rawData);
	
	// Delete X
	public void delete(Coll<?> coll, String id);
}
