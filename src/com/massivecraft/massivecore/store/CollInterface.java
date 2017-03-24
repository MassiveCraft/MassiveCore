package com.massivecraft.massivecore.store;

import com.massivecraft.massivecore.Active;
import com.massivecraft.massivecore.Identified;
import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.Named;
import com.massivecraft.massivecore.predicate.Predicate;
import com.massivecraft.massivecore.xlib.gson.JsonObject;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public interface CollInterface<E extends Entity<E>> extends Named, Active, Identified
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
	
	String fixId(Object oid);
	String fixIdOrThrow(Object oid) throws IllegalArgumentException;
	
	Map<String, E> getId2entity();
	E get(Object oid);
	E get(Object oid, boolean creative);
	E getFixed(String id);
	E getFixed(String id, boolean creative);
	Collection<String> getIds();
	Collection<String> getIdsRemote();
	boolean containsId(Object oid);
	boolean containsIdFixed(String id);
	
	boolean containsEntity(Object entity);
	
	Collection<E> getAll();
	
	List<E> getAll(Iterable<?> oids, Predicate<? super E> where, Comparator<? super E> orderby, Integer limit, Integer offset);
	List<E> getAll(Iterable<?> oids, Predicate<? super E> where, Comparator<? super E> orderby, Integer limit);
	List<E> getAll(Iterable<?> oids, Predicate<? super E> where, Comparator<? super E> orderby);
	List<E> getAll(Iterable<?> oids, Predicate<? super E> where, Integer limit, Integer offset);
	List<E> getAll(Iterable<?> oids, Predicate<? super E> where, Integer limit);
	List<E> getAll(Iterable<?> oids, Comparator<? super E> orderby, Integer limit, Integer offset);
	List<E> getAll(Iterable<?> oids, Comparator<? super E> orderby, Integer limit);
	List<E> getAll(Iterable<?> oids, Predicate<? super E> where);
	List<E> getAll(Iterable<?> oids, Comparator<? super E> orderby);
	List<E> getAll(Iterable<?> oids, Integer limit, Integer offset);
	List<E> getAll(Iterable<?> oids, Integer limit);
	List<E> getAll(Iterable<?> oids);
	
	List<E> getAll(Predicate<? super E> where, Comparator<? super E> orderby, Integer limit, Integer offset);
	List<E> getAll(Predicate<? super E> where, Comparator<? super E> orderby, Integer limit);
	List<E> getAll(Predicate<? super E> where, Comparator<? super E> orderby);
	List<E> getAll(Predicate<? super E> where, Integer limit, Integer offset);
	List<E> getAll(Predicate<? super E> where, Integer limit);
	List<E> getAll(Comparator<? super E> orderby, Integer limit, Integer offset);
	List<E> getAll(Comparator<? super E> orderby, Integer limit);
	List<E> getAll(Predicate<? super E> where);
	List<E> getAll(Comparator<? super E> orderby);
	List<E> getAll(Integer limit, Integer offset);
	List<E> getAll(Integer limit);
		
	// -------------------------------------------- //
	// BEHAVIOR
	// -------------------------------------------- //
	
	boolean isCreative();
	void setCreative(boolean creative);
	
	boolean isLowercasing();
	void setLowercasing(boolean lowercasing);
	
	// A default entity will not be saved.
	// This is often used together with creative collections to save disc space.
	boolean isDefault(E entity);

	int getEntityTargetVersion();
	
	// -------------------------------------------- //
	// COPY AND CREATE
	// -------------------------------------------- //
	
	void copy(E fromo, E too);
	
	// This simply creates and returns a new instance
	// It does not detach/attach or anything. Just creates a new instance.
	E createNewInstance();
	
	// Create new instance with automatic id
	E create();
	// Create new instance with the requested id
	E create(Object oid);
	
	// -------------------------------------------- //
	// ATTACH AND DETACH
	// -------------------------------------------- //
	
	String attach(E entity);
	String attach(E entity, Object oid);
	
	E detachEntity(E entity);
	E detachId(Object oid);
	E detachIdFixed(String id);
	
	void preAttach(E entity, String id);
	void postAttach(E entity, String id);
	
	void preDetach(E entity, String id);
	void postDetach(E entity, String id);
	
	// -------------------------------------------- //
	// IDENTIFIED MODIFICATIONS
	// -------------------------------------------- //

	void putIdentifiedModification(Object oid, Modification modification);
	void putIdentifiedModificationFixed(String id, Modification modification);
	
	void removeIdentifiedModification(Object oid);
	void removeIdentifiedModificationFixed(String id);
	
	// -------------------------------------------- //
	// SYNC LOG
	// -------------------------------------------- //
	
	/*
	public Map<L, Long> lastMtime();
	public Map<L, Object> lastRaw();
	public Set<L> lastDefault();
	public void clearSynclog(Object oid);
	*/
	
	Map<String, Long> getSyncMap(boolean in);
	long getSyncCountFixed(String name, boolean in);
	void addSyncCountFixed(String name, boolean in);
	
	// -------------------------------------------- //
	// SYNC LOWLEVEL IO ACTIONS
	// -------------------------------------------- //
	
	// oid
	E removeAtLocal(Object oid);
	void removeAtRemote(Object oid);
	void saveToRemote(Object oid);
	void loadFromRemote(Object oid, Entry<JsonObject, Long> remoteEntry);
	
	// Fixed id
	E removeAtLocalFixed(String id);
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
