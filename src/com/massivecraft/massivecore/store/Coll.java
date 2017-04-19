package com.massivecraft.massivecore.store;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveCoreMConf;
import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.comparator.ComparatorNaturalOrder;
import com.massivecraft.massivecore.mixin.MixinModification;
import com.massivecraft.massivecore.store.migrator.MigratorUtil;
import com.massivecraft.massivecore.util.ReflectionUtil;
import com.massivecraft.massivecore.util.Txt;
import com.massivecraft.massivecore.xlib.gson.Gson;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonObject;
import com.massivecraft.massivecore.xlib.gson.JsonSyntaxException;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class Coll<E extends Entity<E>> extends CollAbstract<E>
{
	// -------------------------------------------- //
	// GLOBAL REGISTRY
	// -------------------------------------------- //
	
	public final static String TOTAL = "*total*"; 
	
	// All instances registered here are considered inited.
	private static Map<String, Coll<?>> name2instance = new ConcurrentSkipListMap<>(ComparatorNaturalOrder.get());
	
	private static Map<String, Coll<?>> umap = Collections.unmodifiableMap(name2instance);
	private static Set<String> unames = Collections.unmodifiableSet(name2instance.keySet());
	private static Collection<Coll<?>> uinstances = Collections.unmodifiableCollection(name2instance.values());
	
	public static Map<String, Coll<?>> getMap() { return umap; }
	public static Set<String> getNames() { return unames; }
	public static Collection<Coll<?>> getInstances() { return uinstances; }
	public static Collection<SenderColl<?>> getSenderInstances()
	{
		List<SenderColl<?>> ret = new ArrayList<>();
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
	
	protected final String id;
	@Override public String getId() { return this.id; }
	
	protected final String basename;
	@Override public String getBasename() { return this.basename; }
	
	protected final String universe;
	@Override public String getUniverse() { return this.universe; }
	
	protected final Class<E> entityClass;
	@Override public Class<E> getEntityClass() { return this.entityClass; }
	
	// -------------------------------------------- //
	// SUPPORTING SYSTEM
	// -------------------------------------------- //
	
	protected MassivePlugin plugin;
	@Override public MassivePlugin getPlugin() { return this.plugin; }
	public Gson getGson()
	{
		MassivePlugin plugin = this.getPlugin();
		if (plugin == null) return MassiveCore.gson;
		return plugin.getGson();
	}
	
	protected final Db db;
	@Override public Db getDb() { return this.db; }
	
	protected final Object collDriverObject;
	@Override public Object getCollDriverObject() { return this.collDriverObject; }
	
	@Override
	public boolean supportsPusher()
	{
		return this.getDb().supportsPusher();
	}
	
	protected PusherColl pusher;
	
	@Override
	public PusherColl getPusher()
	{
		if (this.pusher == null) this.pusher = this.getDb().getPusher(this);
		return this.pusher;
	}
	
	@Override
	public String getDebugName()
	{
		String ret = this.getPlugin().getName() + "::" + this.getBasename();
		
		if (this.getUniverse() != null)
		{
			ret += "@" + this.getUniverse();
		}
		
		return ret;
	}
	
	// -------------------------------------------- //
	// STORAGE
	// -------------------------------------------- //
	
	// Loaded
	protected Map<String, E> idToEntity;
	
	@Override public Map<String, E> getIdToEntityRaw() { return this.idToEntity; }
	
	@Override public Collection<String> getIdsRemote() { return this.getDb().getIds(this); }
	
	// -------------------------------------------- //
	// BEHAVIOR
	// -------------------------------------------- //
	
	// What entity version do we want?
	protected final int entityTargetVersion;
	@Override public int getEntityTargetVersion() { return this.entityTargetVersion; }
	
	// -------------------------------------------- //
	// IDENTIFIED MODIFICATIONS
	// -------------------------------------------- //
	
	protected Map<String, Modification> identifiedModifications;
	
	@Override
	public synchronized void putIdentifiedModificationFixed(String id, Modification modification)
	{
		if (id == null) throw new NullPointerException("id");
		if (modification == null) throw new NullPointerException("modification");
		Modification old = this.identifiedModifications.get(id);
		if (old != null && modification.getPriority() <= old.getPriority()) return;
		this.identifiedModifications.put(id, modification);
	}
	
	@Override
	public synchronized void removeIdentifiedModificationFixed(String id)
	{
		if (id == null) throw new NullPointerException("id");
		this.identifiedModifications.remove(id);
	}
	
	// -------------------------------------------- //
	// SYNCLOG
	// -------------------------------------------- //

	// Log database synchronization for display in the "/massivecore mstore stats" command.
	private Map<String, Long> id2out = new TreeMap<>();
	private Map<String, Long> id2in = new TreeMap<>();
	
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
	
	@Override
	public synchronized E removeAtLocalFixed(String id)
	{
		if (id == null) throw new NullPointerException("id");
		
		this.removeIdentifiedModificationFixed(id);
		
		E entity = this.idToEntity.remove(id);
		if (entity == null) return null;
		entity.clearSyncLogFields();
		
		// Remove entity reference info
		entity.setContainer(null);
		entity.setId(null);
		
		return entity;
	}
	
	@Override
	public synchronized void removeAtRemoteFixed(String id)
	{
		if (id == null) throw new NullPointerException("id");
		
		this.removeIdentifiedModificationFixed(id);
		
		this.getDb().delete(this, id);
		MixinModification.get().syncModification(this, id);
	}
	
	@Override
	public synchronized void saveToRemoteFixed(String id)
	{
		if (id == null) throw new NullPointerException("id");
		
		this.removeIdentifiedModificationFixed(id);
		
		E entity = this.idToEntity.get(id);
		if (entity == null) return;
		entity.clearSyncLogFields();
		
		JsonObject raw = this.getGson().toJsonTree(entity, this.getEntityClass()).getAsJsonObject();
		entity.setLastRaw(raw);
		
		if (this.isDefault(entity))
		{
			this.getDb().delete(this, id);
			entity.setLastDefault(true);
		}
		else
		{
			long mtime = this.getDb().save(this, id, raw);
			// TODO: This fail should not happen often. We could handle it better though.
			// Perhaps we should log it, or simply try again.
			if (mtime == 0) return;
			entity.setLastMtime(mtime);
		}
		MixinModification.get().syncModification(entity);
	}
	
	@Override
	public synchronized void loadFromRemoteFixed(String id, Entry<JsonObject, Long> remoteEntry)
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
		
		if ( ! this.remoteEntryIsOk(id, remoteEntry)) return;
		JsonObject raw = remoteEntry.getKey();
		Long mtime = remoteEntry.getValue();

		int version = MigratorUtil.getVersion(raw);
		if (version > this.getEntityTargetVersion())
		{
			logLoadError(id, String.format("Cannot load entity of entity version %d", version));
			return;
		}

		// Migrate if another version is wanted
		boolean migrated = MigratorUtil.migrate(this.getEntityClass(), raw, this.getEntityTargetVersion());

		// Calculate temp but handle raw cases.
		E temp;
		
		try
		{
			temp = this.getGson().fromJson(raw, this.getEntityClass());	
		}
		catch (JsonSyntaxException ex)
		{
			logLoadError(id, ex.getMessage());
			return;
		}
		
		E entity = this.getFixed(id, false);
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
			
			// On creation it might be modified by addition or removal of new/old fields.
			// So we must do a check for that.
			// this.putIdentifiedModificationFixed(id, Modification.UNKNOWN);
		}
		
		entity.setLastRaw(raw);
		entity.setLastMtime(mtime);
		entity.setLastDefault(false);

		// Now the loading is done. If it was migrated we will have to save it to remote again.
		if (migrated) this.putIdentifiedModificationFixed(id, Modification.LOCAL_ALTER);
	}
	
	public boolean remoteEntryIsOk(String id, Entry<JsonObject, Long> remoteEntry)
	{
		Long mtime = remoteEntry.getValue();
		if (mtime == null)
		{
			this.logLoadError(id, "Last modification time (mtime) was null. The file might not be readable or simply not exist.");
			return false;
		}
		
		if (mtime == 0)
		{
			this.logLoadError(id, "Last modification time (mtime) was 0. The file might not be readable or simply not exist.");
			return false;
		}
		
		JsonObject raw = remoteEntry.getKey();
		if (raw == null)
		{
			this.logLoadError(id, "Raw data was null. Is the file completely empty?");
			return false;
		}
		if (raw.isJsonNull())
		{
			this.logLoadError(id, "Raw data was JSON null. It seems you have a file containing just the word \"null\". Why would you do this?");
			return false;
		}
		
		return true;
	}
	
	public void logLoadError(String entityId, String error)
	{
		MassiveCore.get().log(Txt.parse("<b>Database could not load entity. You edited a file manually and made wrong JSON?"));
		MassiveCore.get().log(Txt.parse("<k>Entity: <v>%s", entityId));
		MassiveCore.get().log(Txt.parse("<k>Collection: <v>%s", this.getDebugName()));
		MassiveCore.get().log(Txt.parse("<k>Error: <v>%s", error));
	}
	
	// -------------------------------------------- //
	// SYNC DECIDE AND BASIC DO
	// -------------------------------------------- //
	
	@Override
	public Modification examineIdFixed(String id, Long remoteMtime, boolean local, boolean remote)
	{
		if (id == null) throw new NullPointerException("id");
		if (!local && !remote) throw new IllegalArgumentException("Must be either remote or local.");
		
		Modification current = this.identifiedModifications.get(id);
		// DEBUG
		// if (Bukkit.isPrimaryThread())
		// {
		// 	MassiveCore.get().log(Txt.parse("<a>examineId <k>Coll: <v>%s <k>Entity: <v>%s <k>Modification: <v>%s", this.getName(), id, ret));
		// }
		if (current != null && current.hasTopPriority()) return current;
		
		E localEntity = this.idToEntity.get(id);
		if (remoteMtime == null && remote)
		{
			// TODO: This is slow
			remoteMtime = this.getDb().getMtime(this, id);
		}
		
		boolean existsLocal = (localEntity != null);
		boolean existsRemote = remote ? (remoteMtime != 0) : true;
		
		// So we don't have this anywhere?
		if ( ! existsLocal && ! existsRemote) return Modification.UNKNOWN;
		
		// If we have it both locally and remotely...
		if (existsLocal && existsRemote)
		{
			long lastMtime = localEntity.getLastMtime();
			
			// ...and we are looking for remote changes...
			if (remote)
			{
				// ...and it was modified remotely.
				if ( ! remoteMtime.equals(lastMtime)) return Modification.REMOTE_ALTER;	
			}
			// ...and we are looking for local changes
			if (local)
			{
				// ...and it was modified locally.
				if (this.examineHasLocalAlterFixed(id, localEntity)) return Modification.LOCAL_ALTER;	
			}
		}
		// Otherwise, if we only have it locally...
		else if (existsLocal)
		{
			// ...and we look for local changes, and it was default...
			if (local && localEntity.getLastDefault())
			{
				// ...then perhaps it was locally modified
				if (this.examineHasLocalAlterFixed(id, localEntity)) return Modification.LOCAL_ALTER;
			}
			// ...if we look for remote changes, and it wasn't default
			else if (remote && !localEntity.getLastDefault())
			{
				//...then it was remotely detached.
				return Modification.REMOTE_DETACH;
			}
		}
		// If we just have it remotely and look for remote changes. It was attached there.
		else if (remote && existsRemote)
		{
			return Modification.REMOTE_ATTACH;
		}
		
		// No modification was made.
		return Modification.NONE;
	}
	
	protected boolean examineHasLocalAlterFixed(String id, E entity)
	{
		JsonObject lastRaw = entity.getLastRaw();
		JsonObject currentRaw = null;
		
		try
		{
			currentRaw = this.getGson().toJsonTree(entity, this.getEntityClass()).getAsJsonObject();
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
	public Modification syncIdFixed(String id, Modification modification, Entry<JsonObject, Long> remoteEntry)
	{
		if (id == null) throw new NullPointerException("id");
		if (modification == null || modification.isUnknown())
		{
			Long remoteMtime = null;
			if (remoteEntry != null) remoteMtime = remoteEntry.getValue();
			
			Modification actualModification = this.examineIdFixed(id, remoteMtime, true, true);
			if (MassiveCoreMConf.get().warnOnLocalAlter && modification == Modification.UNKNOWN_LOG && actualModification.isModified())
			{
				E entity = this.idToEntity.get(id);
				if (entity != null)
				{
					this.logModification(entity, actualModification);	
				}
			}
			modification = actualModification;
		}
		if (MStore.DEBUG_ENABLED) System.out.println(this.getDebugName() + " syncronising " + modification + " on " + id);
		
		
		// DEBUG
		// MassiveCore.get().log(Txt.parse("<a>syncId <k>Coll: <v>%s <k>Entity: <v>%s <k>Modification: <v>%s", this.getName(), id, modification));
		
		switch (modification)
		{
			case LOCAL_ALTER:
			case LOCAL_ATTACH:
				this.saveToRemoteFixed(id);
				if (this.isActive())
				{
					this.addSyncCountFixed(TOTAL, false);
					this.addSyncCountFixed(id, false);
				}
			break;
			case LOCAL_DETACH:
				this.removeAtRemoteFixed(id);
				if (this.isActive())
				{
					this.addSyncCountFixed(TOTAL, false);
					this.addSyncCountFixed(id, false);
				}
			break;
			case REMOTE_ALTER:
			case REMOTE_ATTACH:
				this.loadFromRemoteFixed(id, remoteEntry);
				if (this.isActive())
				{
					this.addSyncCountFixed(TOTAL, true);
					this.addSyncCountFixed(id, true);
				}
			break;
			case REMOTE_DETACH:
				this.removeAtLocalFixed(id);
				if (this.isActive())
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
	
	protected void logModification(E entity, Modification modification)
	{
		JsonObject lastRaw = entity.getLastRaw();
		
		if (lastRaw == null)
		{
			List<String> messages = new MassiveList<>();
			messages.add(Txt.parse("<pink>%s", this.getDebugName()));
			messages.add(Txt.parse("<aqua>%s", entity.getId()));
			messages.add(Txt.parse("<blue>%s", modification));
			String message = Txt.implode(messages, Txt.parse("<silver> | "));
			message = Txt.parse("<b>[lastRaw null] %s", message);
			MassiveCore.get().log(message);
			return;
		}
		
		JsonObject currentRaw = this.getGson().toJsonTree(entity).getAsJsonObject();
		
		List<String> changes = new MassiveList<>();
		
		// Check removal and modification.
		for (Entry<String, JsonElement> entry : lastRaw.entrySet())
		{
			String name = entry.getKey();
			JsonElement currentValue = currentRaw.get(name);
			if (currentValue == null)
			{
				changes.add(Txt.parse("<b>%s", name));
				continue;
			}
			JsonElement lastValue = entry.getValue();
			if (MStore.equal(currentValue, lastValue)) continue;
			changes.add(Txt.parse("<i>%s", name));
		}
		
		// Check for addition
		for (Entry<String, JsonElement> entry : currentRaw.entrySet())
		{
			String name = entry.getKey();
			if (lastRaw.has(name)) continue;
			changes.add(Txt.parse("<g>%s", name));
		}
		
		// Log
		if (changes.isEmpty()) return;
		changes.add(0, Txt.parse("<pink>%s", this.getDebugName()));
		changes.add(1, Txt.parse("<aqua>%s", entity.getId()));
		String change = Txt.implode(changes, Txt.parse("<silver> | "));
		String message = Txt.parse("<b>[Unreported Modification] %s", change);
		
		MassiveCore.get().log(message);
	}
	
	@Override
	public void identifyModifications(Modification veto)
	{
		// Get remote id2mtime snapshot
		Map<String, Long> id2RemoteMtime = this.getDb().getId2mtime(this);
		
		// Java 8
		//this.idToEntity.keySet().forEach(id -> id2RemoteMtime.putIfAbsent(id, 0));
		
		// Java 8 >
		for (String id : this.idToEntity.keySet())
		{
			if (id2RemoteMtime.containsKey(id)) continue;
			id2RemoteMtime.put(id,  0L);
		}
		
		// Check for modifications
		for (Entry<String, Long> entry : id2RemoteMtime.entrySet())
		{
			this.identifyModificationFixed(entry.getKey(), entry.getValue(), veto);
		}
	}
	
	@Override
	public void identifyModificationFixed(String id, Long remoteMtime, Modification veto)
	{
		if (id == null) throw new NullPointerException("id");
		
		Modification modification = this.examineIdFixed(id, remoteMtime, true, true);
		this.storeModificationIdentification(id, modification, veto);
	}
	
	@Override
	public void identifyLocalModifications(Modification veto)
	{
		for (String id : idToEntity.keySet())
		{
			this.identifyLocalModificationFixed(id, veto);
		}
	}
	
	@Override
	public void identifyLocalModificationFixed(String id, Modification veto)
	{
		if (id == null) throw new NullPointerException("id");
		
		Modification modification = this.examineIdFixed(id, null, true, false);
		this.storeModificationIdentification(id, modification, veto);
	}
	
	@Override
	public void identifyRemoteModifications(Modification veto)
	{
		// Get remote id2mtime snapshot
		Map<String, Long> id2RemoteMtime = this.getDb().getId2mtime(this);
		
		//Note: We must also check local ids, in case of remote detach.
		
		// Java 8
		//this.idToEntity.keySet().forEach(id -> id2RemoteMtime.putIfAbsent(id, 0));
		
		// Java 8 >
		for (String id : this.idToEntity.keySet())
		{
			if (id2RemoteMtime.containsKey(id)) continue;
			id2RemoteMtime.put(id,  0L);
		}
		
		// Check for modifications
		for (Entry<String, Long> entry : id2RemoteMtime.entrySet())
		{
			this.identifyRemoteModificationFixed(entry.getKey(), entry.getValue(), veto);
		}
	}
	
	@Override
	public void identifyRemoteModificationFixed(String id, Long remoteMtime, Modification veto)
	{
		if (id == null) throw new NullPointerException("id");
		
		Modification modification = this.examineIdFixed(id, remoteMtime, false, true);
		this.storeModificationIdentification(id, modification, veto);
	}
	
	protected void storeModificationIdentification(String id, Modification modification, Modification veto)
	{
		if (modification.isModified())
		{
			if (MStore.DEBUG_ENABLED) System.out.println(this.getDebugName() + " identified " + modification + " on " + id);
			if (veto != null && ! modification.isSafe()) modification = veto;
			this.putIdentifiedModificationFixed(id, modification);
		}
	}
	
	@Override
	public void syncIdentified()
	{
		for (Entry<String, Modification> entry : this.identifiedModifications.entrySet())
		{
			String id = entry.getKey();
			Modification modification = entry.getValue();
			this.syncIdFixed(id, modification);
		}
	}
	
	@Override
	public void syncAll()
	{
		this.identifyModifications(null);
		this.syncIdentified();
	}
	
	@Override
	public void initLoadAllFromRemote()
	{
		Map<String, Entry<JsonObject, Long>> idToEntryMap = this.getDb().loadAll(this);
		if (idToEntryMap == null) return;
		
		for (Entry<String, Entry<JsonObject, Long>> idToEntry : idToEntryMap.entrySet())
		{
			String id = idToEntry.getKey();
			Entry<JsonObject, Long> remoteEntry = idToEntry.getValue();
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
		this.syncIdentified();
	}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public Coll(String id, Class<E> entityClass, Db db, MassivePlugin plugin)
	{
		// Plugin
		if (plugin == null) plugin = this.calculatePlugin();
		this.plugin = plugin;
		
		// Entity Class
		if (entityClass == null) entityClass = this.calculateEntityClass();
		this.entityClass = entityClass;
		
		// Id
		if (id == null) id = this.calculateId();
		this.id = id;
		String[] idParts = this.id.split("\\@");
		this.basename = idParts[0];
		if (idParts.length > 1)
		{
			this.universe = idParts[1];
		}
		else
		{
			this.universe = null;
		}
		
		// Db
		if (db == null) db = this.calculateDb();
		this.db = db;
		this.collDriverObject = db.createCollDriverObject(this);
		
		// Collections
		this.idToEntity = new ConcurrentHashMap<>();
		this.identifiedModifications = new ConcurrentHashMap<>();

		// Migration
		int version = 0;
		try
		{
			version = ReflectionUtil.getField(this.getEntityClass(), MigratorUtil.VERSION_FIELD_NAME, this.createNewInstance());
		}
		catch (Exception ex)
		{
			// The field was not there
		}
		this.entityTargetVersion = version;
		
		// Tasks
		this.tickTask = new Runnable()
		{
			@Override public void run() { Coll.this.onTick(); }
		};
	}
	
	public Coll(String id)
	{
		this(id, null, null, null);
	}
	
	public Coll()
	{
		this(null, null, null, null);
	}
	
	public MassivePlugin calculatePlugin()
	{
		// Create
		int retlength = 0;
		MassivePlugin ret = null;
		
		// Fill
		String me = this.getClass().getName();
		for (Plugin plugin : Bukkit.getPluginManager().getPlugins())
		{
			if (!(plugin instanceof MassivePlugin)) continue;
			MassivePlugin mplugin = (MassivePlugin)plugin;
			String you = mplugin.getDescription().getMain();
			
			String prefix = StringUtils.getCommonPrefix(new String[]{me, you});
			if (prefix == null) continue;
			int length = prefix.length();
			if (length <= retlength) continue;
			
			retlength = length;
			ret = mplugin;
		}
		
		// Return
		if (ret == null) throw new RuntimeException("plugin could not be calculated");
		return ret;
	}

	@SuppressWarnings("unchecked")
	public Class<E> calculateEntityClass()
	{
		Class<?> clazz = this.getClass();
		ParameterizedType superType = (ParameterizedType) clazz.getGenericSuperclass();
		Type[] typeArguments = superType.getActualTypeArguments();
		return (Class<E>) typeArguments[0];
	}

	public String calculateId()
	{
		return this.getPlugin().getDescription().getName().toLowerCase() + "_" + this.getEntityClass().getSimpleName().toLowerCase();
	}
	
	public Db calculateDb()
	{
		return MStore.getDb();
	}
	
	@Override
	public Coll<E> getColl()
	{
		return this;
	}
	
	// -------------------------------------------- //
	// ACTIVE
	// -------------------------------------------- //
	
	@Override
	public boolean isActive()
	{
		return name2instance.containsKey(this.getName());
	}
	
	@Override public boolean isLive() { return this.isActive(); }
	
	@Override
	public void setActive(boolean active)
	{
		// NoChange
		if (this.isActive() == active) throw new IllegalStateException("Active Already " + active);
		
		// TODO: Clean up this stuff below. It branches too late.
		if (active)
		{
			MigratorUtil.validateMigratorsPresent(entityClass, 0, this.getEntityTargetVersion());

			if (this.supportsPusher())
			{
				this.getPusher().init();
			}
			
			this.initLoadAllFromRemote();
			//this.syncIdentified();
			
			name2instance.put(this.getName(), this);
		}
		else
		{
			if (this.supportsPusher())
			{
				this.getPusher().deinit();
			}
			
			// TODO: Save outwards only? We may want to avoid loads at this stage...
			this.syncAll();
			
			name2instance.remove(this.getName());
		}
	}
	
	@Override
	public MassivePlugin getActivePlugin()
	{
		return this.plugin;
	}
	
	@Override
	public MassivePlugin setActivePlugin(MassivePlugin plugin)
	{
		MassivePlugin ret = this.plugin;
		this.plugin = plugin;
		return ret;
	}
	
	@Override
	public void setActive(MassivePlugin plugin)
	{
		this.setActivePlugin(plugin);
		this.setActive(plugin != null);
	}

}
