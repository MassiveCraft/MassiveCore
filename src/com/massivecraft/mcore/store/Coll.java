package com.massivecraft.mcore.store;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.plugin.Plugin;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.MPlugin;
import com.massivecraft.mcore.NaturalOrderComparator;
import com.massivecraft.mcore.Predictate;
import com.massivecraft.mcore.store.accessor.Accessor;
import com.massivecraft.mcore.xlib.gson.Gson;
import com.massivecraft.mcore.xlib.gson.JsonElement;

public class Coll<E> implements CollInterface<E>
{
	// -------------------------------------------- //
	// GLOBAL REGISTRY
	// -------------------------------------------- //
	
	// All instances registered here are considered inited.
	private static List<Coll<?>> instances = new CopyOnWriteArrayList<Coll<?>>();
	public static List<Coll<?>> getInstances() { return instances; }
	
	private static TreeSet<String> names = new TreeSet<String>(NaturalOrderComparator.get());
	public static TreeSet<String> getNames() { return names; }
	
	// Log database syncronization for display in the "/mcore mstore stats" command.
	private static Map<String, Long> name2out = new TreeMap<String, Long>(String.CASE_INSENSITIVE_ORDER);
	private static Map<String, Long> name2in = new TreeMap<String, Long>(String.CASE_INSENSITIVE_ORDER);
	
	public static long getSyncCount(String name, boolean in)
	{
		Long count = (in ? name2in.get(name) : name2out.get(name));
		if (count == null) return 0;
		return count;
	}
	
	public static void addSyncCount(String name, boolean in)
	{
		long count = getSyncCount(name, in);
		count++;
		Map<String, Long> map = (in ? name2in : name2out);
		map.put(name, count);
	}
	
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
	
	// -------------------------------------------- //
	// SUPPORTING SYSTEM
	// -------------------------------------------- //
	
	protected Plugin plugin;
	@Override public Plugin getPlugin() { return this.plugin; }
	public Gson getGson()
	{
		if (this.getPlugin() instanceof MPlugin)
		{
			return ((MPlugin)this.getPlugin()).gson;
		}
		else
		{
			return MCore.gson;
		}
	}
	
	protected Db db;
	@Override public Db getDb() { return this.db; }
	@Override public Driver getDriver() { return this.db.getDriver(); }
	
	protected Object collDriverObject;
	@Override public Object getCollDriverObject() { return this.collDriverObject; }
	
	// -------------------------------------------- //
	// STORAGE
	// -------------------------------------------- //
	
	protected Map<String, E> id2entity;
	@Override public Map<String, E> getId2entity() { return Collections.unmodifiableMap(this.id2entity); } 
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
		String id = this.fixId(oid);
		if (id == null) return null;
		E ret = this.id2entity.get(id);
		if (ret != null) return ret;
		if ( ! creative) return null;
		return this.create(id, noteChange);
	}
	
	@Override public Collection<String> getIds() { return Collections.unmodifiableCollection(this.id2entity.keySet()); }
	@Override public Collection<String> getIdsRemote() { return this.getDb().getDriver().getIds(this); }
	@Override
	public boolean containsId(Object oid)
	{
		String id = this.fixId(oid);
		if (id == null) return false;
		return this.id2entity.containsKey(id);
	}
	
	// Get the id for this entity.
	protected Map<E, String> entity2id;
	@Override public Map<E, String> getEntity2id() { return Collections.unmodifiableMap(this.entity2id); }
	@Override public String getId(Object entity) { return this.entity2id.get(entity); }
	@Override public boolean containsEntity(Object entity) { return this.entity2id.containsKey(entity); };
	
	@Override public Collection<E> getAll() { return Collections.unmodifiableCollection(this.entity2id.keySet()); }
	@Override public Collection<E> getAll(Predictate<? super E> where) { return MStoreUtil.uglySQL(this.getAll(), where, null, null, null); }
	@Override public Collection<E> getAll(Predictate<? super E> where, Comparator<? super E> orderby) { return MStoreUtil.uglySQL(this.getAll(), where, orderby, null, null); }
	@Override public Collection<E> getAll(Predictate<? super E> where, Comparator<? super E> orderby, Integer limit) { return MStoreUtil.uglySQL(this.getAll(), where, orderby, limit, null); }
	@Override public Collection<E> getAll(Predictate<? super E> where, Comparator<? super E> orderby, Integer limit, Integer offset) { return MStoreUtil.uglySQL(this.getAll(), where, orderby, limit, offset); }
	
	@Override
	public String fixId(Object oid)
	{
		if (oid == null) return null;
		
		String ret = null;
		if (oid instanceof String) 
		{
			ret = (String)oid;
		}
		else if (oid.getClass() == this.entityClass)
		{
			ret = this.entity2id.get(oid);
		}
		if (ret == null) return null;
		
		return this.isLowercasing() ? ret.toLowerCase() : ret;
	}
	
	// -------------------------------------------- //
	// BEHAVIOR
	// -------------------------------------------- //

	protected boolean creative;
	@Override public boolean isCreative() { return this.creative; }
	@Override public void setCreative(boolean creative) { this.creative = creative; }
	
	// "Lowercasing" means that the ids are always converted to lower case when fixed.
	// This is highly recommended for sender colls.
	// The senderIds are case insensitive by nature and some times you simply can't know the correct casing.
	protected boolean lowercasing;
	@Override public boolean isLowercasing() { return this.lowercasing; }
	@Override public void setLowercasing(boolean lowercasing) { this.lowercasing = lowercasing; }
	
	// Should that instance be saved or not?
	// If it is default it should not be saved.
	@SuppressWarnings("rawtypes")
	@Override public boolean isDefault(E entity)
	{
		if (entity instanceof Entity)
		{
			return ((Entity)entity).isDefault();
		}
		else
		{
			return false;
		}
	}
	
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
	public String attach(E entity)
	{
		return this.attach(entity, null);
	}
	
	@Override
	public synchronized String attach(E entity, Object oid)
	{
		return this.attach(entity, oid, true);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected synchronized String attach(E entity, Object oid, boolean noteChange)
	{
		// Check entity
		if (entity == null) return null;
		String id = this.getId(entity);
		if (id != null) return id;
		
		// Check/Fix id
		if (oid == null)
		{
			id = MStore.createId();
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
		String id = this.fixId(oid);
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
	
	protected Set<String> localAttachIds;
	protected Set<String> localDetachIds;
	protected Set<String> changedIds;
	
	protected synchronized void clearIdentifiedChanges(Object oid)
	{
		String id = this.fixId(oid);
		this.localAttachIds.remove(id);
		this.localDetachIds.remove(id);
		this.changedIds.remove(id);
	}
	
	// -------------------------------------------- //
	// SYNCLOG
	// -------------------------------------------- //

	protected Map<String, Long> lastMtime;
	protected Map<String, JsonElement> lastRaw;
	protected Set<String> lastDefault;
	
	protected synchronized void clearSynclog(Object oid)
	{
		String id = this.fixId(oid);
		this.lastMtime.remove(id);
		this.lastRaw.remove(id);
		this.lastDefault.remove(id);
	}
	
	// -------------------------------------------- //
	// SYNC LOWLEVEL IO ACTIONS
	// -------------------------------------------- //
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public synchronized E removeAtLocal(Object oid)
	{
		String id = this.fixId(oid);
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
	public synchronized void removeAtRemote(Object oid)
	{
		String id = this.fixId(oid);
		
		this.clearIdentifiedChanges(id);
		this.clearSynclog(id);
		
		this.getDb().getDriver().delete(this, id);
	}
	
	@Override
	public synchronized void saveToRemote(Object oid)
	{
		String id = this.fixId(oid);
		
		this.clearIdentifiedChanges(id);
		this.clearSynclog(id);
		
		E entity = this.id2entity.get(id);
		if (entity == null) return;
		
		JsonElement raw = this.getGson().toJsonTree(entity, this.getEntityClass());
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
	public synchronized void loadFromRemote(Object oid)
	{
		String id = this.fixId(oid);
		
		this.clearIdentifiedChanges(id);
		
		Entry<JsonElement, Long> entry = this.getDriver().load(this, id);
		if (entry == null) return;
		
		JsonElement raw = entry.getKey();
		if (raw == null) return;
		
		Long mtime = entry.getValue();
		if (mtime == null) return;
		
		E entity = this.get(id, true, false);
		
		this.copy(this.getGson().fromJson(raw, this.getEntityClass()), entity);
		
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
	public ModificationState examineId(Object oid)
	{
		String id = this.fixId(oid);
		return this.examineId(id, null, false);
	}
	
	@Override
	public ModificationState examineId(Object oid, Long remoteMtime)
	{
		String id = this.fixId(oid);
		return this.examineId(id, remoteMtime, true);
	}
	
	protected ModificationState examineId(Object oid, Long remoteMtime, boolean remoteMtimeSupplied)
	{
		String id = this.fixId(oid);
		
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
	
	protected boolean examineHasLocalAlter(String id, E entity)
	{
		JsonElement lastRaw = this.lastRaw.get(id);
		JsonElement currentRaw = this.getGson().toJsonTree(entity, this.getEntityClass());
		
		return !MStore.equal(lastRaw, currentRaw);
	}
	
	@Override
	public ModificationState syncId(Object oid)
	{
		String id = this.fixId(oid);
		
		ModificationState mstate = this.examineId(id);
		
		//mplugin.log("syncId: It seems", id, "has state", mstate);
		
		switch (mstate)
		{
			case LOCAL_ALTER:
			case LOCAL_ATTACH:
				 this.saveToRemote(id);
				 if (this.inited()) addSyncCount(this.getName(), false);
			break;
			case LOCAL_DETACH:
				this.removeAtRemote(id);
				if (this.inited()) addSyncCount(this.getName(), false);
			break;
			case REMOTE_ALTER:
			case REMOTE_ATTACH:
				this.loadFromRemote(id);
				if (this.inited()) addSyncCount(this.getName(), true);
			break;
			case REMOTE_DETACH:
				this.removeAtLocal(id);
				if (this.inited()) addSyncCount(this.getName(), true);
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
		for (String id : this.changedIds)
		{
			this.syncId(id);
		}
	}
	
	@Override
	public void syncAll()
	{
		// Find all ids
		Set<String> allids = new HashSet<String>(this.id2entity.keySet());
		allids.addAll(this.getDriver().getIds(this));
		for (String id : allids)
		{
			this.syncId(id);
		}
	}
	
	@Override
	public void findSuspects()
	{
		// Get remote id and mtime snapshot
		Map<String, Long> id2RemoteMtime = this.getDb().getDriver().getId2mtime(this);
		
		// Compile a list of all ids (both remote and local)
		Set<String> allids = new HashSet<String>();
		allids.addAll(id2RemoteMtime.keySet());
		allids.addAll(this.id2entity.keySet());
		
		// Check for modifications
		for (String id : allids)
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
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public Coll(String name, Class<E> entityClass, Db db, Plugin plugin, boolean creative, boolean lowercasing, String idStrategyName, Comparator<? super String> idComparator, Comparator<? super E> entityComparator)
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
		this.creative = creative;
		this.lowercasing = lowercasing;
		
		// SUPPORTING SYSTEM
		this.plugin = plugin;
		this.db = db;
		this.collDriverObject = db.getCollDriverObject(this);
		
		// STORAGE
		this.id2entity = new ConcurrentSkipListMap<String, E>(idComparator);
		this.entity2id = new ConcurrentSkipListMap<E, String>(entityComparator);
		
		// IDENTIFIED CHANGES
		this.localAttachIds = new ConcurrentSkipListSet<String>(idComparator);
		this.localDetachIds = new ConcurrentSkipListSet<String>(idComparator);
		this.changedIds = new ConcurrentSkipListSet<String>(idComparator);
		
		// SYNCLOG
		this.lastMtime = new ConcurrentSkipListMap<String, Long>(idComparator);
		this.lastRaw = new ConcurrentSkipListMap<String, JsonElement>(idComparator);
		this.lastDefault = new ConcurrentSkipListSet<String>(idComparator);
		
		final Coll<E> me = this;
		this.tickTask = new Runnable()
		{
			@Override public void run() { me.onTick(); }
		};
	}
	
	public Coll(String name, Class<E> entityClass, Db db, Plugin plugin, boolean creative, boolean lowercasing)
	{
		this(name, entityClass, db, plugin, creative, lowercasing, "uuid", null, null);
	}
	
	public Coll(String name, Class<E> entityClass, Db db, Plugin plugin)
	{
		this(name, entityClass, db, plugin, false, false);
	}
	
	@Override
	public void init()
	{
		if (this.inited()) return;
		// TODO: Could this be more efficient by considering it's the first sync?
		this.syncAll();
		instances.add(this);
		names.add(this.getName());
	}
	
	@Override
	public void deinit()
	{
		if (!this.inited()) return;
		// TODO: Save outwards only? We may want to avoid loads at this stage...
		this.syncAll();
		instances.remove(this);
		names.remove(this.getName());
	}
	
	@Override
	public boolean inited()
	{
		return instances.contains(this);
	}
}
