package com.massivecraft.mcore.store;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

import org.bukkit.plugin.Plugin;

import com.massivecraft.mcore.Predictate;
import com.massivecraft.mcore.store.idstrategy.IdStrategy;
import com.massivecraft.mcore.store.storeadapter.StoreAdapter;

public interface CollInterface<E, L extends Comparable<? super L>>
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
	public Plugin getPlugin();
	
	public Db<?> getDb();
	public Driver<?> getDriver();
	public StoreAdapter getStoreAdapter();
	public IdStrategy<L, ?> getIdStrategy();
	public Object getCollDriverObject();
	
	// -------------------------------------------- //
	// STORAGE
	// -------------------------------------------- //
	public Map<L, E> getId2entity();
	public E get(Object oid);
	public E get(Object oid, boolean creative);
	public Collection<L> getIds();
	public Collection<L> getIdsRemote();
	public boolean containsId(Object oid);
	
	public Map<E, L> getEntity2id();
	public L getId(Object entity);
	public boolean containsEntity(Object entity);
	public Collection<E> getAll();
	public Collection<E> getAll(Predictate<? super E> where);
	public Collection<E> getAll(Predictate<? super E> where, Comparator<? super E> orderby);
	public Collection<E> getAll(Predictate<? super E> where, Comparator<? super E> orderby, Integer limit);
	public Collection<E> getAll(Predictate<? super E> where, Comparator<? super E> orderby, Integer limit, Integer offset);
	
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
	public E detachEntity(Object entity);
	public E detachId(Object oid);
	
	// -------------------------------------------- //
	// IDENTIFIED CHANGES
	// -------------------------------------------- //
	
	/*
	public Set<L> localAttachIds();
	public Set<L> localDetachIds();
	public Set<L> changedIds();
	public void clearIdentifiedChanges(Object oid);
	*/
	
	// -------------------------------------------- //
	// SYNC LOG
	// -------------------------------------------- //
	
	/*
	public Map<L, Long> lastMtime();
	public Map<L, Object> lastRaw();
	public Set<L> lastDefault();
	public void clearSynclog(Object oid);
	*/
	
	// -------------------------------------------- //
	// SYNC LOWLEVEL IO ACTIONS
	// -------------------------------------------- //
	
	public E removeAtLocal(Object oid);
	public void removeAtRemote(Object oid);
	public void saveToRemote(Object oid);
	public void loadFromRemote(Object oid);
	
	// -------------------------------------------- //
	// SYNC EXAMINE AND DO
	// -------------------------------------------- //
	
	public ModificationState examineId(Object oid);
	public ModificationState examineId(Object oid, Long remoteMtime);
	
	public ModificationState syncId(Object oid);
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