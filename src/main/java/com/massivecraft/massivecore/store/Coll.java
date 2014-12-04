package com.massivecraft.massivecore.store;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.HashCodeComparator;
import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.NaturalOrderComparator;
import com.massivecraft.massivecore.Predictate;
import com.massivecraft.massivecore.store.accessor.Accessor;
import com.massivecraft.massivecore.util.Txt;
import com.massivecraft.massivecore.xlib.gson.Gson;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonObject;

public class Coll<E> implements CollInterface<E>
{
	// -------------------------------------------- //
	// GLOBAL REGISTRY
	// -------------------------------------------- //
	
	public final static String TOTAL = "*total*"; 
	
	// All instances registered here are considered inited.
	private static Map<String, Coll<?>> name2instance = new ConcurrentSkipListMap<String, Coll<?>>(NaturalOrderComparator.get());
	
	private static Map<String, Coll<?>> umap = Collections.unmodifiableMap(name2instance);
	private static Set<String> unames = Collections.unmodifiableSet(name2instance.keySet());
	private static Collection<Coll<?>> uinstances = Collections.unmodifiableCollection(name2instance.values());
	
	public static Map<String, Coll<?>> getMap() { return umap; }
	public static Set<String> getNames() { return unames; }
	public static Collection<Coll<?>> getInstances() { return uinstances; }
	
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
		if (this.getPlugin() instanceof MassivePlugin)
		{
			return ((MassivePlugin)this.getPlugin()).gson;
		}
		else
		{
			return MassiveCore.gson;
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
	
	// All
	protected Set<String> ids;
	
	// Loaded
	protected Map<String, E> id2entity;
	protected Map<E, String> entity2id;
	
	@Override public Collection<String> getIds() { return Collections.unmodifiableCollection(this.ids); }
	
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
	
	@Override public Collection<String> getIdsLoaded() { return Collections.unmodifiableCollection(this.id2entity.keySet()); }
	@Override public Collection<String> getIdsRemote() { return this.getDb().getDriver().getIds(this); }
	@Override
	public boolean containsId(Object oid)
	{
		String id = this.fixId(oid);
		if (id == null) return false;
		return this.ids.contains(id);
	}
	
	@Override public Map<E, String> getEntity2id() { return Collections.unmodifiableMap(this.entity2id); }
	@Override public String getId(Object entity) { return this.entity2id.get(entity); }
	@Override public boolean containsEntity(Object entity) { return this.entity2id.containsKey(entity); };
	
	@Override public Collection<E> getAll()
	{
		return Collections.unmodifiableCollection(this.entity2id.keySet());
	}
	@Override public List<E> getAll(Predictate<? super E> where) { return MStoreUtil.uglySQL(this.getAll(), where, null, null, null); }
	@Override public List<E> getAll(Predictate<? super E> where, Comparator<? super E> orderby) { return MStoreUtil.uglySQL(this.getAll(), where, orderby, null, null); }
	@Override public List<E> getAll(Predictate<? super E> where, Comparator<? super E> orderby, Integer limit) { return MStoreUtil.uglySQL(this.getAll(), where, orderby, limit, null); }
	@Override public List<E> getAll(Predictate<? super E> where, Comparator<? super E> orderby, Integer limit, Integer offset) { return MStoreUtil.uglySQL(this.getAll(), where, orderby, limit, offset); }
	
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
	
	protected boolean lazy;
	@Override public boolean isLazy() { return this.lazy; }
	@Override public void setLazy(boolean lazy) { this.lazy = lazy; }
	
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
	
	// This is used in parallel with the isDefault.
	// Parallel usage is useful since we can then override isDeafult just like before.
	public static boolean isCustomDataDefault(Object entity)
	{
		if (!(entity instanceof Entity)) return true;
		JsonObject customData = ((Entity<?>)entity).getCustomData();
		if (customData == null) return true;
		if (customData.entrySet().size() == 0) return true;
		return false;
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
			eto.setCustomData(efrom.getCustomData());
		}
		else if (ofrom instanceof JsonObject)
		{
			JsonObject jfrom = (JsonObject)ofrom;
			JsonObject jto = (JsonObject)oto;
			// Clear To
			Iterator<Entry<String, JsonElement>> iter = jto.entrySet().iterator();
			while (iter.hasNext())
			{
				iter.next();
				iter.remove();
			}
			// Copy
			for (Entry<String, JsonElement> entry : jfrom.entrySet())
			{
				jto.add(entry.getKey(), entry.getValue());
			} 
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
		
		// PRE
		this.preAttach(entity, id);
		
		// Add entity reference info
		if (entity instanceof Entity)
		{
			((Entity)entity).setColl(this);
			((Entity)entity).setId(id);
		}
		
		// Attach
		this.ids.add(id);
		this.id2entity.put(id, entity);
		this.entity2id.put(entity, id);
		
		// Make note of the change
		if (noteChange)
		{
			this.localAttachIds.add(id);
			this.changedIds.add(id);
		}
		
		// POST
		this.postAttach(entity, id);
		
		return id;
	}
		
	@SuppressWarnings("unchecked")
	@Override
	public E detachEntity(Object entity)
	{
		if (entity == null) throw new NullPointerException("entity");
		
		E e = (E)entity;
		String id = this.getId(e);
		if (id == null)
		{
			// It seems the entity is already detached.
			// In such case just silently return it.
			return e;
		}
		
		this.detach(e, id);
		return e;
	}
	
	@Override
	public E detachId(Object oid)
	{
		if (oid == null) throw new NullPointerException("oid");
		
		String id = this.fixId(oid);
		E e = this.get(id, false);
		if (e == null) return null;
		
		this.detach(e, id);
		return e;
	}
	
	private void detach(E entity, String id)
	{
		if (entity == null) throw new NullPointerException("entity");
		if (id == null) throw new NullPointerException("id");
		
		// PRE
		this.preDetach(entity, id);
		
		// Remove @ local
		this.removeAtLocal(id);
		
		// Identify the change
		this.localDetachIds.add(id);
		this.changedIds.add(id);
		
		// POST
		this.postDetach(entity, id);
	}
	
	@Override
	public void preAttach(E entity, String id)
	{
		if (entity instanceof Entity)
		{
			((Entity<?>)entity).preAttach(id);
		}
	}
	
	@Override
	public void postAttach(E entity, String id)
	{
		if (entity instanceof Entity)
		{
			((Entity<?>)entity).postAttach(id);
		}
	}
	
	@Override
	public void preDetach(E entity, String id)
	{
		if (entity instanceof Entity)
		{
			((Entity<?>)entity).preDetach(id);
		}
	}
	
	@Override
	public void postDetach(E entity, String id)
	{
		if (entity instanceof Entity)
		{
			((Entity<?>)entity).postDetach(id);
		}
	}
	
	// -------------------------------------------- //
	// IDENTIFIED CHANGES
	// -------------------------------------------- //
	
	protected Set<String> localAttachIds;
	protected Set<String> localDetachIds;
	protected Set<String> changedIds;
	
	protected synchronized void clearIdentifiedChanges(Object oid)
	{
		if (oid == null) throw new NullPointerException("oid");
		
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
		if (oid == null) throw new NullPointerException("oid");
		
		String id = this.fixId(oid);
		this.lastMtime.remove(id);
		this.lastRaw.remove(id);
		this.lastDefault.remove(id);
	}
	
	// Log database syncronization for display in the "/massivecore mstore stats" command.
	private Map<String, Long> id2out = new TreeMap<String, Long>();
	private Map<String, Long> id2in = new TreeMap<String, Long>();
	
	public Map<String, Long> getSyncMap(boolean in)
	{
		return in ? this.id2in : this.id2out;
	}
	
	public long getSyncCount(String id, boolean in)
	{
		Long count = this.getSyncMap(in).get(id);
		if (count == null) return 0;
		return count;
	}
	
	public void addSyncCount(String id, boolean in)
	{
		long count = this.getSyncCount(id, in);
		count++;
		this.getSyncMap(in).put(id, count);
	}
	
	// -------------------------------------------- //
	// SYNC LOWLEVEL IO ACTIONS
	// -------------------------------------------- //
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public synchronized E removeAtLocal(Object oid)
	{
		if (oid == null) throw new NullPointerException("oid");
		
		String id = this.fixId(oid);
		
		this.clearIdentifiedChanges(id);
		this.clearSynclog(id);
		
		E entity = this.id2entity.remove(id);
		if (entity == null) return null;
		
		this.entity2id.remove(entity);
		
		this.ids.remove(id);
		
		// Remove entity reference info
		if (entity instanceof Entity)
		{
			((Entity)entity).setColl(null);
			((Entity)entity).setId(null);
		}
		
		return entity;
	}
	
	@Override
	public synchronized void removeAtRemote(Object oid)
	{
		if (oid == null) throw new NullPointerException("oid");
		
		String id = this.fixId(oid);
		
		this.clearIdentifiedChanges(id);
		this.clearSynclog(id);
		
		this.getDb().getDriver().delete(this, id);
	}
	
	@Override
	public synchronized void saveToRemote(Object oid)
	{
		if (oid == null) throw new NullPointerException("oid");
		
		String id = this.fixId(oid);
		
		this.clearIdentifiedChanges(id);
		this.clearSynclog(id);
		
		E entity = this.id2entity.get(id);
		if (entity == null) return;
		
		JsonElement raw = this.getGson().toJsonTree(entity, this.getEntityClass());
		this.lastRaw.put(id, raw);
		
		if (this.isDefault(entity) && isCustomDataDefault(entity))
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
	
	@SuppressWarnings("unchecked")
	@Override
	public synchronized void loadFromRemote(Object oid, Entry<JsonElement, Long> entry, boolean entrySupplied)
	{
		if (oid == null) throw new NullPointerException("oid");
		String id = this.fixId(oid);
		
		this.clearIdentifiedChanges(id);
		
		if ( ! entrySupplied)
		{
			try
			{
				entry = this.getDriver().load(this, id);
			}
			catch (Exception e)
			{
				logLoadError(id, e.getMessage());
				return;
			}
		}
		
		if (entry == null)
		{
			logLoadError(id, "MStore driver could not load data entry. The file might not be readable or simply not exist.");
			return;
		}
		
		JsonElement raw = entry.getKey();
		if (raw == null)
		{
			logLoadError(id, "Raw data was null. Is the file completely empty?");
			return;
		}
		if (raw.isJsonNull())
		{
			logLoadError(id, "Raw data was JSON null. It seems you have a file containing just the word \"null\". Why would you do this?");
			return;
		}
		
		Long mtime = entry.getValue();
		if (mtime == null)
		{
			logLoadError(id, "Last modification time (mtime) was null.");
			return;
		}
		
		// Calculate temp but handle raw cases.
		E temp = null;
		if (this.getEntityClass().isAssignableFrom(JsonObject.class))
		{
			temp = (E) raw;
		}
		else
		{
			temp = this.getGson().fromJson(raw, this.getEntityClass());
		}
		E entity = this.get(id, false);
		if (entity != null)
		{
			// It did already exist
			this.copy(temp, entity);
		}
		else
		{
			// Create first
			entity = this.createNewInstance();
			
			// Copy over data first
			this.copy(temp, entity);
			
			// Then attach!
			this.attach(entity, id, false);
		}
		
		this.lastRaw.put(id, raw);
		this.lastMtime.put(id, mtime);
		this.lastDefault.remove(id);
	}
	
	public void logLoadError(String entityId, String error)
	{
		MassiveCore.get().log(Txt.parse("<b>Database could not load entity. You edited a file manually and made wrong JSON?"));
		MassiveCore.get().log(Txt.parse("<k>Entity: <v>%s", entityId));
		MassiveCore.get().log(Txt.parse("<k>Collection: <v>%s", this.getName()));
		MassiveCore.get().log(Txt.parse("<k>Error: <v>%s", error));
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
		JsonElement currentRaw = null;
		
		try
		{
			currentRaw = this.getGson().toJsonTree(entity, this.getEntityClass());
		}
		catch (Exception e)
		{
			MassiveCore.get().log(Txt.parse("<b>Database examineHasLocalAlter failed convert current entity to JSON tree."));
			MassiveCore.get().log(Txt.parse("<k>Error: <v>%s", e.getMessage()));
			MassiveCore.get().log(Txt.parse("<k>Entity: <v>%s", id));
			MassiveCore.get().log(Txt.parse("<k>Collection: <v>%s", this.getName()));
			throw new RuntimeException(e);
		}
		
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
				if (this.inited())
				{
					this.addSyncCount(TOTAL, false);
					this.addSyncCount(id, false);
				}
			break;
			case LOCAL_DETACH:
				this.removeAtRemote(id);
				if (this.inited())
				{
					this.addSyncCount(TOTAL, false);
					this.addSyncCount(id, false);
				}
			break;
			case REMOTE_ALTER:
			case REMOTE_ATTACH:
				this.loadFromRemote(id, null, false);
				if (this.inited())
				{
					this.addSyncCount(TOTAL, true);
					this.addSyncCount(id, true);
				}
			break;
			case REMOTE_DETACH:
				this.removeAtLocal(id);
				if (this.inited())
				{
					this.addSyncCount(TOTAL, true);
					this.addSyncCount(id, true);
				}
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
		/*if (MassiveCore.get().doderp)
		{
			if (this.changedIds.size() > 0)
			{
				System.out.println("Coll " + this.getName() + " had suspects " + Txt.implode(this.changedIds, " "));
			}
		}*/
		
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
		allids.addAll(this.ids);
		
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
	
	@Override
	public void initLoadAllFromRemote()
	{
		Map<String, Entry<JsonElement, Long>> idToEntryMap = this.getDb().getDriver().loadAll(this);
		if (idToEntryMap == null) return;
		
		for (Entry<String, Entry<JsonElement, Long>> idToEntry : idToEntryMap.entrySet())
		{
			String id = idToEntry.getKey();
			Entry<JsonElement, Long> entry = idToEntry.getValue();
			loadFromRemote(id, entry, true);
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
	
	public Coll(String name, Class<E> entityClass, Db db, Plugin plugin, boolean lazy, boolean creative, boolean lowercasing, Comparator<? super String> idComparator, Comparator<? super E> entityComparator)
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
		this.lazy = lazy;
		this.creative = creative;
		this.lowercasing = lowercasing;
		
		// SUPPORTING SYSTEM
		this.plugin = plugin;
		this.db = db;
		this.collDriverObject = db.getCollDriverObject(this);
		
		// STORAGE
		if (entityComparator == null && !Comparable.class.isAssignableFrom(entityClass))
		{
			// Avoid "Classname cannot be cast to java.lang.Comparable" error in ConcurrentSkipListMap
			entityComparator = HashCodeComparator.get();
		}
		this.ids = new ConcurrentSkipListSet<String>(idComparator);
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
	
	public Coll(String name, Class<E> entityClass, Db db, Plugin plugin, boolean lazy, boolean creative, boolean lowercasing)
	{
		this(name, entityClass, db, plugin, lazy, creative, lowercasing, null, null);
	}
	
	public Coll(String name, Class<E> entityClass, Db db, Plugin plugin)
	{
		this(name, entityClass, db, plugin, false, false, false);
	}
	
	@Override
	public void init()
	{
		if (this.inited()) return;
		
		this.initLoadAllFromRemote();
		// this.syncAll();
		
		name2instance.put(this.getName(), this);
	}
	
	@Override
	public void deinit()
	{
		if (!this.inited()) return;
		
		// TODO: Save outwards only? We may want to avoid loads at this stage...
		this.syncAll();
		
		name2instance.remove(this.getName());
	}
	
	@Override
	public boolean inited()
	{
		return name2instance.containsKey(this.getName());
	}
}
