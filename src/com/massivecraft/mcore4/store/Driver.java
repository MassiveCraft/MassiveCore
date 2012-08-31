package com.massivecraft.mcore4.store;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.massivecraft.mcore4.store.idstrategy.IdStrategy;
import com.massivecraft.mcore4.store.storeadapter.StoreAdapter;

public interface Driver<R>
{
	// Returns the name of the driver.
	public String name();
	
	// This is the rawdata format this driver works with.
	// Could for example be JsonElement or DBObject
	public Class<R> getRawdataClass();
	
	// This is some sort of database specific id strategy with built in adapter
	public boolean registerIdStrategy(IdStrategy<?, ?> idStrategy);
	public <L> IdStrategy<L, ?> getIdStrategy(String idStrategyName);
	
	// Get the default store adapter for the driver.
	public StoreAdapter getStoreAdapter();
	
	// Get a database instance from the driver
	public Db<R> db(String uri);
	
	// What collections are in the database?		
	public Set<String> collnames(Db<?> db);
	
	// Is id X in the collection?
	public <L> boolean containsId(Coll<?, L> coll, L id);
	
	// When was X last altered?
	public <L> Long mtime(Coll<?, L> coll, L id);
		
	// What ids are in the collection?
	public <L> Collection<L> ids(Coll<?, L> coll);
	
	// Return a map of all ids with their corresponding mtimes
	public <L> Map<L, Long> id2mtime(Coll<?, L> coll);
	
	// Load the raw data for X. The second part of the entry is the remote mtime at the load.
	public <L> Entry<R, Long> load(Coll<?, L> coll, L id);
	
	// Save raw data as X
	// Return value is the new mtime (we caused the change).
	// If the mtime is null something failed.
	public <L> Long save(Coll<?, L> coll, L id, final Object rawData);
	
	// Delete X
	public <L> void delete(Coll<?, L> coll, L id);
}
