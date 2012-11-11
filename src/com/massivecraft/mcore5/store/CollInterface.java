package com.massivecraft.mcore5.store;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

import com.massivecraft.mcore5.MPlugin;
import com.massivecraft.mcore5.Predictate;
import com.massivecraft.mcore5.store.idstrategy.IdStrategy;
import com.massivecraft.mcore5.store.storeadapter.StoreAdapter;

public interface CollInterface<E, L>
{
	// -------------------------------------------- //
	// WHAT DO WE HANDLE?
	// -------------------------------------------- //
	public String getName();
	public String getBasename();
	public String getUniverse();
	public Class<E> getEntityClass();
	public Class<L> getIdClass();
	
	// -------------------------------------------- //
	// SUPPORTING SYSTEM
	// -------------------------------------------- //
	public MPlugin getMplugin();
	
	public Db<?> getDb();
	public Driver<?> getDriver();
	public StoreAdapter getStoreAdapter();
	public IdStrategy<L, ?> getIdStrategy();
	public Object getCollDriverObject();
	
	// -------------------------------------------- //
	// STORAGE
	// -------------------------------------------- //
	public Collection<L> getIds();
	public Collection<L> getIdsRemote();
	public boolean containsId(Object oid);
	public boolean containsEntity(E entity);
	
	public Collection<E> getAll();
	public Collection<E> getAll(Predictate<E> where);
	public Collection<E> getAll(Predictate<E> where, Comparator<E> orderby);
	public Collection<E> getAll(Predictate<E> where, Comparator<E> orderby, Integer limit);
	public Collection<E> getAll(Predictate<E> where, Comparator<E> orderby, Integer limit, Integer offset);
	
	public Map<L, E> getId2entity();
	public E get(Object oid);
	public E get(Object oid, boolean creative);

	public Map<E, L> getEntity2id();
	public L getId(E entity);
	public L fixId(Object oid);
	
	// -------------------------------------------- //
	// BAHAVIOR
	// -------------------------------------------- //
	public boolean isCreative();
	public void setCreative(boolean val);
	
	// A default entity will not be saved.
	// This is often used together with creative collections to save disc space.
	public boolean isDefault(E entity);
	
	// -------------------------------------------- //
	// COPY AND CREATE
	// -------------------------------------------- //
	
	public void copy(Object fromo, Object too);
	
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
	
	public ModificationState syncId(L id);
	public void syncSuspects();
	public void syncAll();
	public void findSuspects();
	
	// -------------------------------------------- //
	// SYNC RUNNABLES / SCHEDULING
	// -------------------------------------------- //
	
	// The tickTask simply runs the onTick method.
	public Runnable getTickTask();
	public void onTick();
	
	public Thread examineThread();
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public void init();
	public boolean inited();
	
	
}