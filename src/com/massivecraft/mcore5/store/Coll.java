package com.massivecraft.mcore5.store;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

import com.massivecraft.mcore5.MCore;
import com.massivecraft.mcore5.MPlugin;
import com.massivecraft.mcore5.Predictate;
import com.massivecraft.mcore5.store.accessor.Accessor;
import com.massivecraft.mcore5.store.idstrategy.IdStrategy;
import com.massivecraft.mcore5.store.storeadapter.StoreAdapter;

public class Coll<E, L extends Comparable<? super L>> implements CollInterface<E, L>
{
	// -------------------------------------------- //
	// GLOBAL REGISTRY
	// -------------------------------------------- //
	
	public static List<Coll<?, ?>> instances = new CopyOnWriteArrayList<Coll<?, ?>>(); 
	
	// -------------------------------------------- //
	// WHAT DO WE HANDLE?
	// -------------------------------------------- //
	
	protected final String name;
	@Override public String getName() { return this.name; }
	
	protected final String basename;
	@Override public String getBasename() { return this.basename; }
	
	protected final String universe;
	@Override public String getUniverse() { return this.universe; }
	
	protected final Class<E> entityClass;
	@Override public Class<E> getEntityClass() { return this.entityClass; }

	protected final Class<L> idClass;
	@Override public Class<L> getIdClass() { return this.idClass; }
	
	// -------------------------------------------- //
	// SUPPORTING SYSTEM
	// -------------------------------------------- //
	
	protected MPlugin mplugin;
	@Override public MPlugin getMplugin() { return this.mplugin; }
	
	protected Db<?> db;
	@Override public Db<?> getDb() { return this.db; }
	@Override public Driver<?> getDriver() { return this.db.getDriver(); }
	
	protected IdStrategy<L, ?> idStrategy;
	@Override public IdStrategy<L, ?> getIdStrategy() { return this.idStrategy; }
	
	protected StoreAdapter storeAdapter;
	@Override public StoreAdapter getStoreAdapter() { return this.storeAdapter; }
	
	protected Object collDriverObject;
	@Override public Object getCollDriverObject() { return this.collDriverObject; }
	
	// -------------------------------------------- //
	// STORAGE
	// -------------------------------------------- //
	
	protected Map<L, E> id2entity;
	@Override public Map<L, E> getId2entity() { return Collections.unmodifiableMap(this.id2entity); } 
	@Override 
	public E get(Object oid) 
	{
		return this.get(oid, this.isCreative());
	}
	@Override
	public E get(Object oid, boolean creative)
	{
		return this.get(oid, creative, true);
	}
	protected E get(Object oid, boolean creative, boolean noteChange)
	{
		L id = this.fixId(oid);
		if (id == null) return null;
		E ret = this.id2entity.get(id);
		if (ret != null) return ret;
		if ( ! creative) return null;
		return this.create(id, noteChange);
	}
	
	@Override public Collection<L> getIds() { return Collections.unmodifiableCollection(this.id2entity.keySet()); }
	@Override public Collection<L> getIdsRemote() { return this.getDb().getDriver().getIds(this); }
	@Override
	public boolean containsId(Object oid)
	{
		L id = this.fixId(oid);
		if (id == null) return false;
		return this.id2entity.containsKey(id);
	}
	
	// Get the id for this entity.
	protected Map<E, L> entity2id;
	@Override public Map<E, L> getEntity2id() { return Collections.unmodifiableMap(this.entity2id); }
	@Override public L getId(Object entity) { return this.entity2id.get(entity); }
	@Override public boolean containsEntity(Object entity) { return this.entity2id.containsKey(entity); };
	
	@Override public Collection<E> getAll() { return Collections.unmodifiableCollection(this.entity2id.keySet()); }
	@Override public Collection<E> getAll(Predictate<? super E> where) { return MStoreUtil.uglySQL(this.getAll(), where, null, null, null); }
	@Override public Collection<E> getAll(Predictate<? super E> where, Comparator<? super E> orderby) { return MStoreUtil.uglySQL(this.getAll(), where, orderby, null, null); }
	@Override public Collection<E> getAll(Predictate<? super E> where, Comparator<? super E> orderby, Integer limit) { return MStoreUtil.uglySQL(this.getAll(), where, orderby, limit, null); }
	@Override public Collection<E> getAll(Predictate<? super E> where, Comparator<? super E> orderby, Integer limit, Integer offset) { return MStoreUtil.uglySQL(this.getAll(), where, orderby, limit, offset); }
	
	@Override
	public L fixId(Object oid)
	{
		if (oid == null) return null;
		if (oid.getClass() == this.idClass) return this.idClass.cast(oid);
		if (oid.getClass() == this.entityClass) return this.entity2id.get(oid);
		return null;
	}
	
	// -------------------------------------------- //
	// BAHAVIOR
	// -------------------------------------------- //

	protected boolean creative;
	@Override public boolean isCreative() { return this.creative; }
	@Override public void setCreative(boolean val) { this.creative = val; }
	
	// Should that instance be saved or not?
	// If it is default it should not be saved.
	@Override public boolean isDefault(E entity) { return false; }
	
	// -------------------------------------------- //
	// COPY AND CREATE
	// -------------------------------------------- //
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void copy(Object ofrom, Object oto)
	{
		if (ofrom == null) throw new NullPointerException("ofrom");
		if (oto == null) throw new NullPointerException("oto");
			
		if (ofrom instanceof Entity)
		{
			Entity efrom = (Entity)ofrom;
			Entity eto = (Entity)oto;
			
			eto.load(efrom);
		}
		else
		{
			Accessor.get(this.getEntityClass()).copy(ofrom, oto);
		}
	}
	
	// This simply creates and returns a new instance
	// It does not detach/attach or anything. Just creates a new instance.
	@Override
	public E createNewInstance()
	{
		try
		{
			return this.entityClass.newInstance();
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	// Create new instance with automatic id
	@Override
	public E create()
	{
		return this.create(null);
	}
	
	// Create new instance with the requested id
	@Override
	public synchronized E create(Object oid)
	{
		return this.create(oid, true);
	}
	
	public synchronized E create(Object oid, boolean noteChange)
	{
		E entity = this.createNewInstance();
		if (this.attach(entity, oid, noteChange) == null) return null;
		return entity;
	}
	
	// -------------------------------------------- //
	// ATTACH AND DETACH
	// -------------------------------------------- //
	
	@Override
	public L attach(E entity)
	{
		return this.attach(entity, null);
	}
	
	@Override
	public synchronized L attach(E entity, Object oid)
	{
		return this.attach(entity, oid, true);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected synchronized L attach(E entity, Object oid, boolean noteChange)
	{
		// Check entity
		if (entity == null) return null;
		L id = this.getId(entity);
		if (id != null) return id;
		
		// Check/Fix id
		if (oid == null)
		{
			id = this.getIdStrategy().generate(this);
		}
		else
		{
			id = this.fixId(oid);
			if (id == null) return null;
			if (this.id2entity.containsKey(id)) return null;
		}
		
		// Add entity reference info
		if (entity instanceof Entity)
		{
			((Entity)entity).setColl(this);
			((Entity)entity).setid(id);
		}
		
		// Attach
		this.id2entity.put(id, entity);
		this.entity2id.put(entity, id);
		
		// Make note of the change
		if (noteChange)
		{
			this.localAttachIds.add(id);
			this.changedIds.add(id);
		}
		
		return id;
	}
		
	@Override
	public E detachEntity(Object entity)
	{
		return this.detachId(this.getId(entity));
	}
	
	@Override
	public E detachId(Object oid)
	{
		L id = this.fixId(oid);
		if (id == null) return null;
		
		// Remove @ local
		E ret = this.removeAtLocal(id);
		
		// Identify the change
		this.localDetachIds.add(id);
		this.changedIds.add(id);
		
		return ret;
	}
	
	// -------------------------------------------- //
	// IDENTIFIED CHANGES
	// -------------------------------------------- //
	
	protected Set<L> localAttachIds;
	protected Set<L> localDetachIds;
	protected Set<L> changedIds;
	
	protected synchronized void clearIdentifiedChanges(L id)
	{
		this.localAttachIds.remove(id);
		this.localDetachIds.remove(id);
		this.changedIds.remove(id);
	}
	
	// -------------------------------------------- //
	// SYNCLOG
	// -------------------------------------------- //

	protected Map<L, Long> lastMtime;
	protected Map<L, Object> lastRaw;
	protected Set<L> lastDefault;
	
	protected synchronized void clearSynclog(L id)
	{
		this.lastMtime.remove(id);
		this.lastRaw.remove(id);
		this.lastDefault.remove(id);
	}
	
	// -------------------------------------------- //
	// SYNC LOWLEVEL IO ACTIONS
	// -------------------------------------------- //
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public synchronized E removeAtLocal(L id)
	{
		this.clearIdentifiedChanges(id);
		this.clearSynclog(id);
		
		E entity = this.id2entity.remove(id);
		if (entity == null) return null;
		
		this.entity2id.remove(entity);
		
		// Remove entity reference info
		if (entity instanceof Entity)
		{
			((Entity)entity).setColl(null);
			((Entity)entity).setid(null);
		}
		
		return entity;
	}
	
	@Override
	public synchronized void removeAtRemote(L id)
	{
		this.clearIdentifiedChanges(id);
		this.clearSynclog(id);
		
		this.getDb().getDriver().delete(this, id);
	}
	
	@Override
	public synchronized void saveToRemote(L id)
	{
		this.clearIdentifiedChanges(id);
		this.clearSynclog(id);
		
		E entity = this.id2entity.get(id);
		if (entity == null) return;
		
		Object raw = this.getStoreAdapter().read(this, entity);
		this.lastRaw.put(id, raw);
		
		if (this.isDefault(entity))
		{
			this.db.getDriver().delete(this, id);
			this.lastDefault.add(id);
		}
		else
		{
			Long mtime = this.db.getDriver().save(this, id, raw);
			if (mtime == null) return; // This fail should not happen often. We could handle it better though.
			this.lastMtime.put(id, mtime);
		}
	}
	
	@Override
	public synchronized void loadFromRemote(L id)
	{
		this.clearIdentifiedChanges(id);
		
		Entry<?, Long> entry = this.getDb().getDriver().load(this, id);
		if (entry == null) return;
		
		Object raw = entry.getKey();
		if (raw == null) return;
		
		Long mtime = entry.getValue();
		if (mtime == null) return;
		
		E entity = this.get(id, true, false);
		
		this.getStoreAdapter().write(this, raw, entity);
		
		// this.lastRaw.put(id, this.getStoreAdapter().read(this, entity));
		// Store adapter again since result of a database read may be "different" from entity read.
		// WARN: This was causing many issues with config files not updating etc.
		// The approach below may not work with MongoDB at all since that is not tested.
		this.lastRaw.put(id, raw);
		
		this.lastMtime.put(id, mtime);
		this.lastDefault.remove(id);
	}
	
	// -------------------------------------------- //
	// SYNC DECIDE AND BASIC DO
	// -------------------------------------------- //
	
	@Override
	public ModificationState examineId(L id)
	{
		return this.examineId(id, null, false);
	}
	
	@Override
	public ModificationState examineId(L id, Long remoteMtime)
	{
		return this.examineId(id, remoteMtime, true);
	}
	
	protected ModificationState examineId(L id, Long remoteMtime, boolean remoteMtimeSupplied)
	{
		if (this.localDetachIds.contains(id)) return ModificationState.LOCAL_DETACH;
		if (this.localAttachIds.contains(id)) return ModificationState.LOCAL_ATTACH;
		
		E localEntity = this.id2entity.get(id);
		if ( ! remoteMtimeSupplied)
		{
			remoteMtime = this.getDriver().getMtime(this, id);
		}
		
		boolean existsLocal = (localEntity != null);
		boolean existsRemote = (remoteMtime != null);
		
		if ( ! existsLocal && ! existsRemote) return ModificationState.UNKNOWN;
		
		if (existsLocal && existsRemote)
		{
			Long lastMtime = this.lastMtime.get(id);
			if (remoteMtime.equals(lastMtime) == false) return ModificationState.REMOTE_ALTER;
			
			if (this.examineHasLocalAlter(id, localEntity)) return ModificationState.LOCAL_ALTER;
		}
		else if (existsLocal)
		{
			if (this.lastDefault.contains(id))
			{
				if (this.examineHasLocalAlter(id, localEntity)) return ModificationState.LOCAL_ALTER;
			}
			else
			{
				return ModificationState.REMOTE_DETACH;
			}
		}
		else if (existsRemote)
		{
			return ModificationState.REMOTE_ATTACH;
		}
		
		return ModificationState.NONE;
	}
	protected boolean examineHasLocalAlter(L id, E entity)
	{
		Object lastRaw = this.lastRaw.get(id);
		Object currentRaw = this.storeAdapter.read(this, entity);
		return (this.getDriver().equal(currentRaw, lastRaw) == false);
	}
	
	@Override
	public ModificationState syncId(L id)
	{
		ModificationState mstate = this.examineId(id);
		
		//mplugin.log("syncId: It seems", id, "has state", mstate);
		
		switch (mstate)
		{
			case LOCAL_ALTER:
			case LOCAL_ATTACH:
				 this.saveToRemote(id);
			break;
			case LOCAL_DETACH:
				this.removeAtRemote(id);
			break;
			case REMOTE_ALTER:
			case REMOTE_ATTACH:
				this.loadFromRemote(id);
			break;
			case REMOTE_DETACH:
				this.removeAtLocal(id);
			break;
			default:
				this.clearIdentifiedChanges(id);
			break;
		}
		
		return mstate;
	}
	
	@Override
	public void syncSuspects()
	{
		for (L id : this.changedIds)
		{
			this.syncId(id);
		}
	}
	
	@Override
	public void syncAll()
	{
		// Find all ids
		Set<L> allids = new HashSet<L>(this.id2entity.keySet());
		allids.addAll(this.getDriver().getIds(this));
		for (L id : allids)
		{
			this.syncId(id);
		}
	}
	
	@Override
	public void findSuspects()
	{
		// Get remote id and mtime snapshot
		Map<L, Long> id2RemoteMtime = this.getDb().getDriver().getId2mtime(this);
		
		// Compile a list of all ids (both remote and local)
		Set<L> allids = new HashSet<L>();
		allids.addAll(id2RemoteMtime.keySet());
		allids.addAll(this.id2entity.keySet());
		
		// Check for modifications
		for (L id : allids)
		{
			Long remoteMtime = id2RemoteMtime.get(id);
			ModificationState state = this.examineId(id, remoteMtime);
			//mplugin.log("findSuspects: It seems", id, "has state", state);
			if (state.isModified())
			{
				//System.out.println("It seems "+id+" has state "+state);
				this.changedIds.add(id);
			}
		}
	}
	
	// -------------------------------------------- //
	// SYNC RUNNABLES / SCHEDULING
	// -------------------------------------------- //
	
	protected Runnable tickTask;
	@Override public Runnable getTickTask() { return this.tickTask; }
	@Override
	public void onTick()
	{
		this.syncSuspects();
	}
	
	protected ExamineThread<E, L> examineThread;
	@Override public Thread examineThread() { return this.examineThread; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public Coll(Db<?> db, MPlugin mplugin, String idStrategyName, String name, Class<E> entityClass, Class<L> idClass, boolean creative, Comparator<? super L> idComparator, Comparator<? super E> entityComparator)
	{
		// Setup the name and the parsed parts
		this.name = name;
		String[] nameParts = this.name.split("\\@");
		this.basename = nameParts[0];
		if (nameParts.length > 1)
		{
			this.universe = nameParts[1];
		}
		else
		{
			this.universe = null;
		}
		
		// WHAT DO WE HANDLE?
		this.entityClass = entityClass;
		this.idClass = idClass;
		this.creative = creative;
		
		// SUPPORTING SYSTEM
		this.mplugin = mplugin;
		this.db = db;
		this.storeAdapter = this.db.getDriver().getStoreAdapter();
		this.idStrategy = this.db.getDriver().getIdStrategy(idStrategyName);
		if (this.idStrategy == null)
		{
			throw new IllegalArgumentException("UNKNOWN: The id stragegy \""+idStrategyName+"\" is unknown to the driver \""+db.getDriver().getName()+"\".");
		}
		else if (this.idStrategy.getLocalClass() != idClass)
		{
			throw new IllegalArgumentException("MISSMATCH: The id stragegy \""+idStrategyName+"\" for the driver \""+db.getDriver().getName()+"\" uses \""+this.idStrategy.getLocalClass().getSimpleName()+"\" but the collection "+this.name+"/"+this.getClass().getSimpleName()+" uses \""+idClass.getSimpleName()+"\".");
		}
		this.collDriverObject = db.getCollDriverObject(this);
		
		// STORAGE
		this.id2entity = new ConcurrentSkipListMap<L, E>(idComparator);
		this.entity2id = new ConcurrentSkipListMap<E, L>(entityComparator);
		
		// IDENTIFIED CHANGES
		this.localAttachIds = new ConcurrentSkipListSet<L>(idComparator);
		this.localDetachIds = new ConcurrentSkipListSet<L>(idComparator);
		this.changedIds = new ConcurrentSkipListSet<L>(idComparator);
		
		// SYNCLOG
		this.lastMtime = new ConcurrentSkipListMap<L, Long>(idComparator);
		this.lastRaw = new ConcurrentSkipListMap<L, Object>(idComparator);
		this.lastDefault = new ConcurrentSkipListSet<L>(idComparator);
		
		final Coll<E, L> me = this;
		this.tickTask = new Runnable()
		{
			@Override public void run() { me.onTick(); }
		};
	}
	
	public Coll(Db<?> db, MPlugin mplugin, String idStrategyName, String name, Class<E> entityClass, Class<L> idClass, boolean creative)
	{
		this(db, mplugin, idStrategyName, name, entityClass, idClass, creative, null, null);
	}
	
	public Coll(MPlugin mplugin, String idStrategyName, String name, Class<E> entityClass, Class<L> idClass, boolean creative)
	{
		this(MCore.getDb(), mplugin, idStrategyName, name, entityClass, idClass, creative);
	}
	
	@Override
	public void init()
	{
		if (this.inited()) return;
		this.syncAll();
		this.examineThread = new ExamineThread<E, L>(this);
		this.examineThread.start();
		instances.add(this);
	}
	
	@Override
	public boolean inited()
	{
		return instances.contains(this);
	}
}
