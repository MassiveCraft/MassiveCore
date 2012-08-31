package com.massivecraft.mcore4.store;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

import com.massivecraft.mcore4.MPlugin;
import com.massivecraft.mcore4.Predictate;
import com.massivecraft.mcore4.store.idstrategy.IdStrategy;
import com.massivecraft.mcore4.store.storeadapter.StoreAdapter;

public interface CollInterface<E, L>
{
	// -------------------------------------------- //
	// WHAT DO WE HANDLE?
	// -------------------------------------------- //
	public String name();
	public Class<E> entityClass();
	public Class<L> idClass();
	
	// -------------------------------------------- //
	// SUPPORTING SYSTEM
	// -------------------------------------------- //
	public MPlugin mplugin();
	
	public Db<?> db();
	public Driver<?> driver();
	public StoreAdapter storeAdapter();
	public IdStrategy<L, ?> idStrategy();
	public Object collDriverObject();
	
	// -------------------------------------------- //
	// STORAGE
	// -------------------------------------------- //
	public Collection<L> ids();
	public Collection<L> idsRemote();
	
	public Collection<E> getAll();
	public Collection<E> getAll(Predictate<E> where);
	public Collection<E> getAll(Predictate<E> where, Comparator<E> orderby);
	public Collection<E> getAll(Predictate<E> where, Comparator<E> orderby, Integer limit);
	public Collection<E> getAll(Predictate<E> where, Comparator<E> orderby, Integer limit, Integer offset);
	
	public Map<L, E> id2entity();
	public E get(Object oid);
	public E get(Object oid, boolean creative);
	public E getBestMatch(Object oid);
	
	public Map<E, L> entity2id();
	public L id(E entity);
	public L idFix(Object oid);
	
	// -------------------------------------------- //
	// BAHAVIOR
	// -------------------------------------------- //
	public boolean creative();
	public void creative(boolean val);
	
	// A default entity will not be saved.
	// This is often used together with creative collections to save disc space.
	public boolean isDefault(E entity);
	
	// -------------------------------------------- //
	// CREATE
	// -------------------------------------------- //
	
	// This simply creates and returns a new instance
	// It does not detach/attach or anything. Just creates a new instance.
	public E createNewInstance();
	
	// Create new instance with automatic id
	public E create();
	// Create new instance with the requested id
	public E create(Object oid);
	
	// -------------------------------------------- //
	// ATTACH AND DETACH
	// -------------------------------------------- //
	public L attach(E entity);
	public L attach(E entity, Object oid);
	public E detach(Object o);
	
	// -------------------------------------------- //
	// IDENTIFIED CHANGES
	// -------------------------------------------- //
	
	/*
	public Set<L> localAttachIds();
	public Set<L> localDetachIds();
	public Set<L> changedIds();
	public void clearIdentifiedChanges(L id);
	*/
	
	// -------------------------------------------- //
	// SYNC LOG
	// -------------------------------------------- //
	
	/*
	public Map<L, Long> lastMtime();
	public Map<L, Object> lastRaw();
	public Set<L> lastDefault();
	public void clearSynclog(L id);
	*/
	
	// -------------------------------------------- //
	// SYNC LOWLEVEL IO ACTIONS
	// -------------------------------------------- //
	
	public E removeAtLocal(L id);
	public void removeAtRemote(L id);
	public void saveToRemote(L id);
	public void loadFromRemote(L id);
	
	// -------------------------------------------- //
	// SYNC EXAMINE AND DO
	// -------------------------------------------- //
	
	public ModificationState examineId(L id);
	public ModificationState examineId(L id, Long remoteMtime);
	
	public void syncId(L id);
	public void syncSuspects();
	public void syncAll();
	public void findSuspects();
	
	// -------------------------------------------- //
	// SYNC RUNNABLES / SCHEDULING
	// -------------------------------------------- //
	
	// The tickTask simply runs the onTick method.
	public Runnable tickTask();
	public void onTick();
	
	public Thread examineThread();
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public void init();
	
	
}