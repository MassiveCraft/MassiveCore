package com.massivecraft.massivecore.store;

import com.massivecraft.massivecore.Active;
import com.massivecraft.massivecore.Identified;
import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.Named;
import com.massivecraft.massivecore.xlib.gson.JsonObject;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

public interface CollInterface<E extends Entity<E>> extends Named, Active, Identified, EntityContainer<E>
{
	// -------------------------------------------- //
	// WHAT DO WE HANDLE?
	// -------------------------------------------- //
	
	String getId();
	String getBasename();
	String getUniverse();
	Class<E> getEntityClass();
	
	// -------------------------------------------- //
	// SUPPORTING SYSTEM
	// -------------------------------------------- //
	
	MassivePlugin getPlugin();
	
	Db getDb();
	Object getCollDriverObject();
	
	boolean supportsPusher();
	PusherColl getPusher();
	
	String getDebugName();
	
	// -------------------------------------------- //
	// STORAGE
	// -------------------------------------------- //
	
	Collection<String> getIdsRemote();
		
	// -------------------------------------------- //
	// BEHAVIOR
	// -------------------------------------------- //

	int getEntityTargetVersion();

	// -------------------------------------------- //
	// SYNC LOG
	// -------------------------------------------- //
	
	Map<String, Long> getSyncMap(boolean in);
	long getSyncCountFixed(String name, boolean in);
	void addSyncCountFixed(String name, boolean in);
	
	// -------------------------------------------- //
	// SYNC LOWLEVEL IO ACTIONS
	// -------------------------------------------- //
	
	// oid
	void removeAtRemote(Object oid);
	void saveToRemote(Object oid);
	void loadFromRemote(Object oid, Entry<JsonObject, Long> remoteEntry);
	
	// Fixed id
	void removeAtRemoteFixed(String id);
	void saveToRemoteFixed(String id);
	void loadFromRemoteFixed(String id, Entry<JsonObject, Long> remoteEntry);
	
	// -------------------------------------------- //
	// SYNC EXAMINE AND DO
	// -------------------------------------------- //
	
	// oid
	Modification examineId(Object oid, Long remoteMtime, boolean local, boolean remote);
	
	// Fixed id
	Modification examineIdFixed(String id, Long remoteMtime, boolean local, boolean remote);
	
	// Sync
	Modification syncId(Object oid);
	Modification syncId(Object oid, Modification modification);
	Modification syncId(Object oid, Modification modification, Entry<JsonObject, Long> remoteEntry);
	
	// Sync fixed
	Modification syncIdFixed(String id);
	Modification syncIdFixed(String id, Modification modification);
	Modification syncIdFixed(String id, Modification modification, Entry<JsonObject, Long> remoteEntry);
	
	void syncIdentified();
	void syncAll();
	
	// Identity
	void identifyModifications(Modification veto);
	void identifyModificationFixed(String id, Long remoteMtime, Modification veto);
	
	void identifyLocalModifications(Modification veto);
	void identifyLocalModificationFixed(String id, Modification veto);
	
	void identifyRemoteModifications(Modification veto);
	void identifyRemoteModificationFixed(String id, Long remoteMtime, Modification veto);
	
	// Init
	void initLoadAllFromRemote();
	
	// -------------------------------------------- //
	// SYNC RUNNABLES / SCHEDULING
	// -------------------------------------------- //
	
	// The tickTask simply runs the onTick method.
	Runnable getTickTask();
	void onTick();
	
}
