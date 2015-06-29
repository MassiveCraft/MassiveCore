package com.massivecraft.massivecore.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.NaturalOrderComparator;
import com.massivecraft.massivecore.Predictate;
import com.massivecraft.massivecore.store.accessor.Accessor;
import com.massivecraft.massivecore.util.Txt;
import com.massivecraft.massivecore.xlib.gson.Gson;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonObject;

public class Coll<E> extends CollAbstract<E>
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
	public static Collection<SenderColl<?>> getSenderInstances()
	{
		List<SenderColl<?>> ret = new ArrayList<SenderColl<?>>();
		for (Coll<?> coll : getInstances())
		{
			if ( ! (coll instanceof SenderColl)) continue;
			SenderColl<?> senderColl = (SenderColl<?>)coll;
			ret.add(senderColl);
		}
		return ret;
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
	
	protected Object collDriverObject;
	@Override public Object getCollDriverObject() { return this.collDriverObject; }
	
	// -------------------------------------------- //
	// STORAGE
	// -------------------------------------------- //
	
	// Loaded
	protected Map<String, E> id2entity;
	protected Map<E, String> entity2id;
	
	@Override public Map<String, E> getId2entity() { return Collections.unmodifiableMap(this.id2entity); } 
	@Override
	public E getFixed(String id, boolean creative)
	{
		return this.getFixed(id, creative, true);
	}
	protected E getFixed(String id, boolean creative, boolean noteModification)
	{
		if (id == null) return null;
		E ret = this.id2entity.get(id);
		if (ret != null) return ret;
		if ( ! creative) return null;
		return this.create(id, noteModification);
	}
	
	@Override public Collection<String> getIds() { return Collections.unmodifiableCollection(this.id2entity.keySet()); }
	@Override public Collection<String> getIdsRemote() { return this.getDb().getIds(this); }
	@Override
	public boolean containsIdFixed(String id)
	{
		if (id == null) return false;
		return this.id2entity.containsKey(id);
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
		if (oid instanceof String) ret = (String) oid;
		else if (oid.getClass() == this.getEntityClass()) ret = this.entity2id.get(oid);
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
		else if (ofrom instanceof JsonObject)
		{
			JsonObject jfrom = (JsonObject)ofrom;
			JsonObject jto = (JsonObject)oto;
			// Clear To
			jto.entrySet().clear();
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
	// TODO: Would it ever make sense for this to fail?
	// Should we just throw an exception immediately if it fails?
	@Override
	public E createNewInstance()
	{
		try
		{
			return this.getEntityClass().newInstance();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
			return null;
		}
	}
	
	// Create new instance with the requested id
	@Override
	public synchronized E create(Object oid)
	{
		return this.create(oid, true);
	}
	
	public synchronized E create(Object oid, boolean noteModification)
	{
		E entity = this.createNewInstance();
		if (this.attach(entity, oid, noteModification) == null) return null;
		return entity;
	}
	
	// -------------------------------------------- //
	// ATTACH AND DETACH
	// -------------------------------------------- //
	
	@Override
	public String attach(E entity, Object oid)
	{
		return this.attach(entity, oid, true);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected synchronized String attach(E entity, Object oid, boolean noteModification)
	{
		// Check entity
		if (entity == null) return null;
		String previousEntityId = this.getId(entity);
		if (previousEntityId != null) return previousEntityId;
		
		String id;
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
		this.id2entity.put(id, entity);
		this.entity2id.put(entity, id);
		
		// Identify Modification
		if (noteModification)
		{
			this.identifiedModifications.put(id, Modification.LOCAL_ATTACH);
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
		
		this.detachFixed(e, id);
		return e;
	}
	
	@Override
	public E detachIdFixed(String id)
	{
		if (id == null) throw new NullPointerException("id");
		
		E e = this.get(id, false);
		if (e == null) return null;
		
		this.detachFixed(e, id);
		return e;
	}
	
	private void detachFixed(E entity, String id)
	{
		if (entity == null) throw new NullPointerException("entity");
		if (id == null) throw new NullPointerException("id");
		
		// PRE
		this.preDetach(entity, id);
		
		// Remove @ local
		this.removeAtLocalFixed(id);
		
		// Identify Modification
		this.identifiedModifications.put(id, Modification.LOCAL_DETACH);
		
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
	// IDENTIFIED MODIFICATIONS
	// -------------------------------------------- //
	
	protected Map<String, Modification> identifiedModifications;
	
	protected synchronized void removeIdentifiedModificationFixed(String id)
	{
		if (id == null) throw new NullPointerException("id");
		this.identifiedModifications.remove(id);
	}
	
	// -------------------------------------------- //
	// SYNCLOG
	// -------------------------------------------- //
	
	// The strings are the ids.
	protected Map<String, Long> lastMtime;
	protected Map<String, JsonElement> lastRaw;
	protected Set<String> lastDefault;
	
	protected synchronized void clearSynclogFixed(String id)
	{
		if (id == null) throw new NullPointerException("id");
		
		this.lastMtime.remove(id);
		this.lastRaw.remove(id);
		this.lastDefault.remove(id);
	}
	
	// Log database synchronization for display in the "/massivecore mstore stats" command.
	private Map<String, Long> id2out = new TreeMap<String, Long>();
	private Map<String, Long> id2in = new TreeMap<String, Long>();
	
	@Override
	public Map<String, Long> getSyncMap(boolean in)
	{
		return in ? this.id2in : this.id2out;
	}
	
	@Override
	public long getSyncCountFixed(String id, boolean in)
	{
		Long count = this.getSyncMap(in).get(id);
		if (count == null) return 0;
		return count;
	}
	
	@Override
	public void addSyncCountFixed(String id, boolean in)
	{
		long count = this.getSyncCountFixed(id, in);
		count++;
		this.getSyncMap(in).put(id, count);
	}
	
	// -------------------------------------------- //
	// SYNC LOWLEVEL IO ACTIONS
	// -------------------------------------------- //
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public synchronized E removeAtLocalFixed(String id)
	{
		if (id == null) throw new NullPointerException("id");
		
		this.removeIdentifiedModificationFixed(id);
		this.clearSynclogFixed(id);
		
		E entity = this.id2entity.remove(id);
		if (entity == null) return null;
		
		this.entity2id.remove(entity);
		
		// Remove entity reference info
		if (entity instanceof Entity)
		{
			((Entity)entity).setColl(null);
			((Entity)entity).setId(null);
		}
		
		return entity;
	}
	
	@Override
	public synchronized void removeAtRemoteFixed(String id)
	{
		if (id == null) throw new NullPointerException("id");
		
		this.removeIdentifiedModificationFixed(id);
		this.clearSynclogFixed(id);
		
		this.getDb().delete(this, id);
	}
	
	@Override
	public synchronized void saveToRemoteFixed(String id)
	{
		if (id == null) throw new NullPointerException("id");
		
		this.removeIdentifiedModificationFixed(id);
		this.clearSynclogFixed(id);
		
		E entity = this.id2entity.get(id);
		if (entity == null) return;
		
		JsonElement raw = this.getGson().toJsonTree(entity, this.getEntityClass());
		this.lastRaw.put(id, raw);
		
		if (this.isDefault(entity))
		{
			this.getDb().delete(this, id);
			this.lastDefault.add(id);
		}
		else
		{
			long mtime = this.getDb().save(this, id, raw);
			if (mtime == 0) return; // This fail should not happen often. We could handle it better though.
			this.lastMtime.put(id, mtime);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public synchronized void loadFromRemoteFixed(String id, Entry<JsonElement, Long> remoteEntry)
	{
		if (id == null) throw new NullPointerException("id");
		
		this.removeIdentifiedModificationFixed(id);
		
		if (remoteEntry == null)
		{
			try
			{
				remoteEntry = this.getDb().load(this, id);
			}
			catch (Exception e)
			{
				logLoadError(id, e.getMessage());
				return;
			}
		}
		
		Long mtime = remoteEntry.getValue();
		if (mtime == null)
		{
			logLoadError(id, "Last modification time (mtime) was null. The file might not be readable or simply not exist.");
			return;
		}
		
		if (mtime == 0)
		{
			logLoadError(id, "Last modification time (mtime) was 0. The file might not be readable or simply not exist.");
			return;
		}
		
		JsonElement raw = remoteEntry.getKey();
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
	public Modification examineIdFixed(String id, Long remoteMtime)
	{
		if (id == null) throw new NullPointerException("id");
		// Local Attach and Detach has the top priority.
		// Otherwise newly attached entities would be removed thinking it was a remote detach.
		// Otherwise newly detached entities would be loaded thinking it was a remote attach.
		Modification ret = this.identifiedModifications.get(id);
		// DEBUG
		// if (Bukkit.isPrimaryThread())
		// {
		// 	MassiveCore.get().log(Txt.parse("<a>examineId <k>Coll: <v>%s <k>Entity: <v>%s <k>Modification: <v>%s", this.getName(), id, ret));
		// }
		if (ret == Modification.LOCAL_ATTACH || ret == Modification.LOCAL_DETACH) return ret;
		
		E localEntity = this.id2entity.get(id);
		if (remoteMtime == null)
		{
			// TODO: This is slow
			remoteMtime = this.getDb().getMtime(this, id);
		}
		
		boolean existsLocal = (localEntity != null);
		boolean existsRemote = (remoteMtime != 0);
		
		if ( ! existsLocal && ! existsRemote) return Modification.UNKNOWN;
		
		if (existsLocal && existsRemote)
		{
			Long lastMtime = this.lastMtime.get(id);
			if (remoteMtime.equals(lastMtime) == false) return Modification.REMOTE_ALTER;
			
			if (this.examineHasLocalAlterFixed(id, localEntity)) return Modification.LOCAL_ALTER;
		}
		else if (existsLocal)
		{
			if (this.lastDefault.contains(id))
			{
				if (this.examineHasLocalAlterFixed(id, localEntity)) return Modification.LOCAL_ALTER;
			}
			else
			{
				return Modification.REMOTE_DETACH;
			}
		}
		else if (existsRemote)
		{
			return Modification.REMOTE_ATTACH;
		}
		
		return Modification.NONE;
	}
	
	protected boolean examineHasLocalAlterFixed(String id, E entity)
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
	public Modification syncIdFixed(String id, Modification modification, Entry<JsonElement, Long> remoteEntry)
	{
		if (id == null) throw new NullPointerException("id");
		if (modification == null || modification == Modification.UNKNOWN)
		{
			Long remoteMtime = null;
			if (remoteEntry != null) remoteMtime = remoteEntry.getValue();
			
			modification = this.examineIdFixed(id, remoteMtime);
		}
		
		// DEBUG
		// MassiveCore.get().log(Txt.parse("<a>syncId <k>Coll: <v>%s <k>Entity: <v>%s <k>Modification: <v>%s", this.getName(), id, modification));
		
		switch (modification)
		{
			case LOCAL_ALTER:
			case LOCAL_ATTACH:
				this.saveToRemoteFixed(id);
				if (this.inited())
				{
					this.addSyncCountFixed(TOTAL, false);
					this.addSyncCountFixed(id, false);
				}
			break;
			case LOCAL_DETACH:
				this.removeAtRemoteFixed(id);
				if (this.inited())
				{
					this.addSyncCountFixed(TOTAL, false);
					this.addSyncCountFixed(id, false);
				}
			break;
			case REMOTE_ALTER:
			case REMOTE_ATTACH:
				this.loadFromRemoteFixed(id, remoteEntry);
				if (this.inited())
				{
					this.addSyncCountFixed(TOTAL, true);
					this.addSyncCountFixed(id, true);
				}
			break;
			case REMOTE_DETACH:
				this.removeAtLocalFixed(id);
				if (this.inited())
				{
					this.addSyncCountFixed(TOTAL, true);
					this.addSyncCountFixed(id, true);
				}
			break;
			default:
				this.removeIdentifiedModificationFixed(id);
			break;
		}
		
		return modification;
	}
	
	@Override
	public void identifyModifications()
	{
		// Get remote id2mtime snapshot
		Map<String, Long> id2RemoteMtime = this.getDb().getId2mtime(this);
		
		// Compile a list of all ids (both remote and local)
		Set<String> allids = new HashSet<String>();
		allids.addAll(id2RemoteMtime.keySet());
		allids.addAll(this.id2entity.keySet());
		
		// Check for modifications
		for (String id : allids)
		{
			Long remoteMtime = id2RemoteMtime.get(id);
			if (remoteMtime == null) remoteMtime = 0L;
			
			Modification modification = this.examineIdFixed(id, remoteMtime);
			if (modification.isModified())
			{
				this.identifiedModifications.put(id, modification);
			}
		}
	}
	
	@Override
	public void syncIdentified(boolean safe)
	{
		for (Entry<String, Modification> entry : this.identifiedModifications.entrySet())
		{
			String id = entry.getKey();
			Modification modification = entry.getValue();
			if (safe)
			{
				modification = null;
			}
			this.syncIdFixed(id, modification);
		}
	}
	
	@Override
	public void syncAll()
	{
		this.identifyModifications();
		this.syncIdentified(false);
	}
	
	@Override
	public void initLoadAllFromRemote()
	{
		Map<String, Entry<JsonElement, Long>> idToEntryMap = this.getDb().loadAll(this);
		if (idToEntryMap == null) return;
		
		for (Entry<String, Entry<JsonElement, Long>> idToEntry : idToEntryMap.entrySet())
		{
			String id = idToEntry.getKey();
			Entry<JsonElement, Long> remoteEntry = idToEntry.getValue();
			loadFromRemoteFixed(id, remoteEntry);
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
		this.syncIdentified(true);
	}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public Coll(String name, Class<E> entityClass, Db db, Plugin plugin, boolean creative, boolean lowercasing, Comparator<? super String> idComparator, Comparator<? super E> entityComparator)
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
		this.collDriverObject = db.createCollDriverObject(this);
		
		// STORAGE
		this.id2entity = entityComparator != null ? new ConcurrentSkipListMap<String, E>(idComparator) : new ConcurrentHashMap<String, E>();
		this.entity2id = entityComparator != null ? new ConcurrentSkipListMap<E, String>(entityComparator) : new ConcurrentHashMap<E, String>();
		
		// IDENTIFIED MODIFICATIONS
		this.identifiedModifications = new ConcurrentHashMap<String, Modification>();
		
		// SYNCLOG
		this.lastMtime = new ConcurrentHashMap<String, Long>();
		this.lastRaw = new ConcurrentHashMap<String, JsonElement>();
		this.lastDefault = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
		
		this.tickTask = new Runnable()
		{
			@Override public void run() { Coll.this.onTick(); }
		};
	}
	
	public Coll(String name, Class<E> entityClass, Db db, Plugin plugin, boolean creative, boolean lowercasing)
	{
		this(name, entityClass, db, plugin, creative, lowercasing, null, null);
	}
	
	public Coll(String name, Class<E> entityClass, Db db, Plugin plugin)
	{
		this(name, entityClass, db, plugin, false, false);
	}
	
	@Override
	public void init()
	{
		if (this.inited()) return; // TODO: Would throwing an exception make more sense?
		
		this.initLoadAllFromRemote();
		// this.syncAll();
		
		name2instance.put(this.getName(), this);
	}
	
	@Override
	public void deinit()
	{
		if ( ! this.inited()) return; // TODO: Would throwing an exception make more sense?
		
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
