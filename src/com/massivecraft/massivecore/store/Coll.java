package com.massivecraft.massivecore.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.util.Txt;
import com.massivecraft.massivecore.xlib.gson.Gson;
import com.massivecraft.massivecore.xlib.gson.JsonElement;

public class Coll<E extends Entity<E>> extends CollAbstract<E>
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
			return ((MassivePlugin)this.getPlugin()).getGson();
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
	protected Map<String, E> id2entity;
	protected Map<E, String> entity2id;
	
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
	
	protected int localPollInfrequency = MStore.DEFAULT_LOCAL_POLL_INFREQUENCY;
	@Override public int getLocalPollInfrequency() { return this.localPollInfrequency; }
	@Override public void setLocalPollInfrequency(int infrequence) { this.localPollInfrequency = infrequence; }
	
	// We often try to call Entity#changed to inform that an entity has been changed locally.
	// And on some Colls we expect it to always be done.
	// However we cannot be sure, but if we expect to always do it
	// then we tell the collection to notify us if we failed to call Entity#changed.
	protected boolean warnOnLocalAlter = false;
	@Override public boolean isWarningOnLocalAlter() { return this.warnOnLocalAlter; }
	@Override public void setWarnOnLocalAlter(boolean warnOnLocalAlter) { this.warnOnLocalAlter = warnOnLocalAlter; }
	
	// Should that instance be saved or not?
	// If it is default it should not be saved.
	@Override public boolean isDefault(E entity)
	{
		return entity.isDefault();
	}

	// -------------------------------------------- //
	// COPY AND CREATE
	// -------------------------------------------- //
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void copy(E ofrom, E oto)
	{
		if (ofrom == null) throw new NullPointerException("ofrom");
		if (oto == null) throw new NullPointerException("oto");
		
		Entity efrom = (Entity)ofrom;
		Entity eto = (Entity)oto;
		eto.load(efrom);
	}
	
	// This simply creates and returns a new instance
	// It does not detach/attach or anything. Just creates a new instance.
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
		entity.setColl(this);
		entity.setId(id);
		
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
	
	@Override
	public E detachEntity(E entity)
	{
		if (entity == null) throw new NullPointerException("entity");

		String id = this.getId(entity);
		if (id == null)
		{
			// It seems the entity is already detached.
			// In such case just silently return it.
			return entity;
		}
		
		this.detachFixed(entity, id);
		return entity;
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
		entity.preAttach(id);
	}
	
	@Override
	public void postAttach(E entity, String id)
	{
		entity.postAttach(id);
	}
	
	@Override
	public void preDetach(E entity, String id)
	{
		entity.preDetach(id);
	}
	
	@Override
	public void postDetach(E entity, String id)
	{
		entity.postDetach(id);
	}
	
	// -------------------------------------------- //
	// IDENTIFIED MODIFICATIONS
	// -------------------------------------------- //
	
	protected Map<String, Modification> identifiedModifications;
	
	public synchronized void putIdentifiedModificationFixed(String id, Modification modification)
	{
		if (id == null) throw new NullPointerException("id");
		if (modification == null) throw new NullPointerException("modification");
		Modification old = this.identifiedModifications.get(id);
		if (old != null && modification.getPriority() <= old.getPriority()) return;
		this.identifiedModifications.put(id, modification);
	}
	
	protected synchronized void removeIdentifiedModificationFixed(String id)
	{
		if (id == null) throw new NullPointerException("id");
		this.identifiedModifications.remove(id);
	}
	
	// -------------------------------------------- //
	// SYNCLOG
	// -------------------------------------------- //

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
	
	@Override
	public synchronized E removeAtLocalFixed(String id)
	{
		if (id == null) throw new NullPointerException("id");
		
		this.removeIdentifiedModificationFixed(id);
		
		E entity = this.id2entity.remove(id);
		if (entity == null) return null;
		entity.clearSyncLogFields();
		
		this.entity2id.remove(entity);
		
		// Remove entity reference info
		entity.setColl(null);
		entity.setId(null);
		
		return entity;
	}
	
	@Override
	public synchronized void removeAtRemoteFixed(String id)
	{
		if (id == null) throw new NullPointerException("id");
		
		this.removeIdentifiedModificationFixed(id);
		
		this.getDb().delete(this, id);
		Mixin.syncModification(this, id);
	}
	
	@Override
	public synchronized void saveToRemoteFixed(String id)
	{
		if (id == null) throw new NullPointerException("id");
		
		this.removeIdentifiedModificationFixed(id);
		
		E entity = this.id2entity.get(id);
		if (entity == null) return;
		entity.clearSyncLogFields();
		
		JsonElement raw = this.getGson().toJsonTree(entity, this.getEntityClass());
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
		Mixin.syncModification(entity);
	}
	
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
		
		JsonElement raw = remoteEntry.getKey();
		Long mtime = remoteEntry.getValue();
		if ( ! this.remoteEntryIsOk(id, remoteEntry)) return;
		
		// Calculate temp but handle raw cases.
		E temp = this.getGson().fromJson(raw, this.getEntityClass());
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
		}
		
		entity.setLastRaw(raw);
		entity.setLastMtime(mtime);
		entity.setLastDefault(false);
	}
	
	public boolean remoteEntryIsOk(String id, Entry<JsonElement, Long> remoteEntry)
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
		
		JsonElement raw = remoteEntry.getKey();
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
		// Meta might be non-existing. But then we create it here.
		// If it is examined then it will be attached anyways.
		Modification current = this.identifiedModifications.get(id);
		// DEBUG
		// if (Bukkit.isPrimaryThread())
		// {
		// 	MassiveCore.get().log(Txt.parse("<a>examineId <k>Coll: <v>%s <k>Entity: <v>%s <k>Modification: <v>%s", this.getName(), id, ret));
		// }
		if (current != null && current.hasTopPriority()) return current;
		
		E localEntity = this.id2entity.get(id);
		if (remoteMtime == null)
		{
			// TODO: This is slow
			remoteMtime = this.getDb().getMtime(this, id);
		}
		
		boolean existsLocal = (localEntity != null);
		boolean existsRemote = (remoteMtime != 0);
		
		// So we don't have this anywhere?
		if ( ! existsLocal && ! existsRemote) return Modification.UNKNOWN;
		
		// If we have it both locally and remotely.
		if (existsLocal && existsRemote)
		{
			long lastMtime = localEntity.getLastMtime();
			
			// If mtime is not equal remote takes priority, and we assume it is altered.
			if ( ! remoteMtime.equals(lastMtime)) return Modification.REMOTE_ALTER;
			
			// Else we check for a local alter.
			if (this.examineHasLocalAlterFixed(id, localEntity)) return Modification.LOCAL_ALTER;
		}
		// If we just have it locally...
		else if (existsLocal)
		{
			// ...and it was default and thus not saved to the db...
			if (localEntity.getLastDefault())
			{
				// ...and also actually altered.
				// Then it is a local alter.
				if (this.examineHasLocalAlterFixed(id, localEntity)) return Modification.LOCAL_ALTER;
			}
			// ...otherwise it was detached remotely.
			else
			{
				return Modification.REMOTE_DETACH;
			}
		}
		// If we just have it remotely. It was attached there.
		else if (existsRemote)
		{
			return Modification.REMOTE_ATTACH;
		}
		
		// No modification was made.
		return Modification.NONE;
	}
	
	@Override
	public Modification examineIdLocalFixed(final String id)
	{
		if (id == null) throw new NullPointerException("id");
		
		// A local entity should have a meta.
		Modification current = this.identifiedModifications.get(id);
		if (current != null && current.hasTopPriority()) return current;
		
		E localEntity = this.id2entity.get(id);
		
		// If not existing, then wtf.
		if (localEntity == null) return Modification.UNKNOWN;
		
		// Altered locally.
		if (this.examineHasLocalAlterFixed(id, localEntity)) return Modification.LOCAL_ALTER;
		
		// Not altered locally.
		return Modification.NONE;
	}
	
	@Override
	public Modification examineIdRemoteFixed(final String id, Long remoteMtime)
	{
		if (id == null) throw new NullPointerException("id");
		
		// We will always know beforehand, when local attach and detach is done.
		// Because they are performed by calling a method on this coll.
		// Meta might be non-existing. But then we create it here.
		// If it is examined then it will be attached anyways.
		Modification current = this.identifiedModifications.get(id);
		if (current != null && current.hasTopPriority()) return current;
		
		if (remoteMtime == null)
		{
			// TODO: This is slow
			remoteMtime = this.getDb().getMtime(this, id);
		}
		E localEntity = this.id2entity.get(id);
		
		boolean existsLocal = (localEntity != null);
		boolean existsRemote = (remoteMtime != 0);
		
		// So we don't have this anywhere?
		if ( ! existsLocal && ! existsRemote) return Modification.UNKNOWN;
		
		// If time is different
		// then it is remotely altered
		if (existsLocal && existsRemote && ! remoteMtime.equals(localEntity.getLastMtime())) return Modification.REMOTE_ALTER;
		
		// If it doesn't exist remotely, and it wasn't because it was default. It was detached remotely.
		if (!existsRemote && existsLocal && ! localEntity.getLastDefault()) return Modification.REMOTE_DETACH;
		
		// If it doesn't exist locally, it was attached remotely.
		if (!existsLocal) return Modification.REMOTE_ATTACH;
		
		// No modification spotted.
		return Modification.NONE;
		
	}
	
	protected boolean examineHasLocalAlterFixed(String id, E entity)
	{
		JsonElement lastRaw = entity.getLastRaw();
		JsonElement currentRaw = null;
		
		try
		{
			currentRaw = this.getGson().toJsonTree(entity);
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
		if (MStore.DEBUG_ENABLED) System.out.println(this.getDebugName() + " syncronising " + modification + " on " + id);
		
		
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
	public void identifyModifications(boolean sure)
	{
		if (MStore.DEBUG_ENABLED) System.out.println(this.getDebugName() + " polling for all changes");
		
		// Get remote id2mtime snapshot
		Map<String, Long> id2RemoteMtime = this.getDb().getId2mtime(this);
		
		// Java 8
		//this.id2entity.keySet().forEach(id -> id2RemoteMtime.putIfAbsent(id, 0));
		
		// Java 8 >
		for (String id : this.id2entity.keySet())
		{
			if (id2RemoteMtime.containsKey(id)) continue;
			id2RemoteMtime.put(id,  0L);
		}
		
		// Check for modifications
		for (Entry<String, Long> entry : id2RemoteMtime.entrySet())
		{
			this.identifyModificationFixed(entry.getKey(), entry.getValue(), sure);
		}
	}
	
	@Override
	public void identifyModificationFixed(String id, Long remoteMtime, boolean sure)
	{
		if (id == null) throw new NullPointerException("id");
		
		Modification modification = this.examineIdFixed(id, remoteMtime);
		this.storeModificationIdentification(id, modification, sure);
	}
	
	@Override
	public void identifyLocalModifications(boolean sure)
	{
		if (MStore.DEBUG_ENABLED) System.out.println(this.getDebugName() + " polling for local changes");
		for (String id : id2entity.keySet())
		{
			this.identifyLocalModificationFixed(id, sure);
		}
	}
	
	@Override
	public void identifyLocalModificationFixed(String id, boolean sure)
	{
		if (id == null) throw new NullPointerException("id");
		
		Modification modification = this.examineIdLocalFixed(id);
		this.storeModificationIdentification(id, modification, sure);
	}
	
	@Override
	public void identifyRemoteModifications(boolean sure)
	{
		if (MStore.DEBUG_ENABLED) System.out.println(this.getDebugName() + " polling for remote changes");
		// Get remote id2mtime snapshot
		Map<String, Long> id2RemoteMtime = this.getDb().getId2mtime(this);
		
		//Note: We must also check local ids, in case of remote detach.
		
		// Java 8
		//this.id2entity.keySet().forEach(id -> id2RemoteMtime.putIfAbsent(id, 0));
		
		// Java 8 >
		for (String id : this.id2entity.keySet())
		{
			if (id2RemoteMtime.containsKey(id)) continue;
			id2RemoteMtime.put(id,  0L);
		}
		
		// Check for modifications
		for (Entry<String, Long> entry : id2RemoteMtime.entrySet())
		{
			this.identifyRemoteModificationFixed(entry.getKey(), entry.getValue(), sure);
		}
	}
	
	@Override
	public void identifyRemoteModificationFixed(String id, Long remoteMtime, boolean sure)
	{
		if (id == null) throw new NullPointerException("id");
		
		Modification modification = this.examineIdRemoteFixed(id, remoteMtime);
		this.storeModificationIdentification(id, modification, sure);
	}
	
	protected void storeModificationIdentification(String id, Modification modification, boolean sure)
	{
		if (this.isWarningOnLocalAlter() && modification == Modification.LOCAL_ALTER)
		{
			MassiveCore.get().log(
				"A local alter was made in " + this.getName() + " on " + id,
				"This was unintended, the developers should be informed."
				);
		}
		
		if (modification.isModified())
		{
			if (MStore.DEBUG_ENABLED) System.out.println(this.getDebugName() + " identified " + modification + " on " + id);
			if (!sure && ! modification.isSafe()) modification = Modification.UNKNOWN;
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
		this.identifyModifications(true);
		this.syncIdentified();
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
		this.syncIdentified();
	}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public Coll(String name, Class<E> entityClass, Db db, Plugin plugin, boolean creative, boolean lowercasing, boolean sorted)
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
		this.id2entity = (sorted) ? new ConcurrentSkipListMap<String, E>(NaturalOrderComparator.get()) : new ConcurrentHashMap<String, E>();
		this.entity2id = (Entity.class.isAssignableFrom(entityClass) && sorted) ? new ConcurrentSkipListMap<E, String>((Comparator<? super E>) ComparatorEntityId.get()) : new ConcurrentHashMap<E, String>();
		
		// ENTITY DATA
		this.identifiedModifications = new ConcurrentHashMap<String, Modification>();
		
		this.tickTask = new Runnable()
		{
			@Override public void run() { Coll.this.onTick(); }
		};
	}
	
	public Coll(String name, Class<E> entityClass, Db db, Plugin plugin)
	{
		this(name, entityClass, db, plugin, false, false, false);
	}
	
	@Override
	public void init()
	{
		if (this.inited()) return; // TODO: Would throwing an exception make more sense?
		
		if (this.supportsPusher())
		{
			//this.getPusher().init();
		}
		
		this.initLoadAllFromRemote();
		// this.syncAll();
		
		name2instance.put(this.getName(), this);
	}
	
	@Override
	public void deinit()
	{
		if ( ! this.inited()) return; // TODO: Would throwing an exception make more sense?
		
		if (this.supportsPusher())
		{
			//this.getPusher().deinit();
		}
		
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
