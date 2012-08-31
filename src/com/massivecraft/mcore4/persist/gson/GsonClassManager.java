package com.massivecraft.mcore4.persist.gson;

import java.io.File;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;

import com.massivecraft.mcore4.Predictate;
import com.massivecraft.mcore4.persist.IClassManager;
import com.massivecraft.mcore4.persist.Persist;
import com.massivecraft.mcore4.util.DiscUtil;
import com.massivecraft.mcore4.xlib.gson.Gson;

public abstract class GsonClassManager<T> implements IClassManager<T>
{
	// -------------------------------------------- //
	// NON INTERFACE
	// -------------------------------------------- //
	protected final static String DOTJSON = ".json";
	protected Set<String> ids;
	protected Set<T> entities;
	protected Map<String, T> id2entity;
	protected Map<T, String> entity2id;
	protected long idCurrent = 1;
	
	protected Gson gson;
	public Gson getGson() { return gson; }
	public void setGson(Gson gson) { this.gson = gson; }

	protected File folder;
	public File getFolder() { return folder; }
	public void setFolder(File val) { this.folder = val; this.folder.mkdirs(); }

	protected boolean creative;
	@Override
	public boolean getIsCreative() { return this.creative; }
	@Override
	public void setIsCreative(boolean val) { this.creative = val; }
	
	protected boolean didLoadAll = false;
	protected void loadIds()
	{
		for(File file : this.getFolder().listFiles(JsonFileFilter.getInstance()))
		{
			this.ids.add(this.idFromFile(file));
		}
	}
	
	protected String idFromFile(File file)
	{
		if (file == null) return null;
		String name = file.getName();
		return name.substring(0, name.length()-5);
	}
	
	protected File fileFromId(Object oid)
	{
		String id = this.idFix(oid);
		if (id == null) return null;
		return new File(this.getFolder(), id+DOTJSON);
	}
	
	// -------------------------------------------- //
	// CONSTRUCTORS
	// -------------------------------------------- //
	
	public GsonClassManager(Gson gson, File folder, boolean creative, boolean lazy, Set<String> ids, Set<T> entities, Map<String, T> id2entity, Map<T, String> entity2id)
	{
		this.gson = gson;
		this.folder = folder;
		this.creative = creative;
		this.ids = ids;
		this.entities = entities;
		this.id2entity = id2entity;
		this.entity2id = entity2id;
		
		this.getFolder().mkdirs();
		this.loadIds();
		if ( ! lazy)
		{
			this.loadAll();
		}
	}
	
	public GsonClassManager(Gson gson, File folder, boolean creative, boolean lazy)
	{
		this(
		gson,
		folder,
		creative,
		lazy,
		new ConcurrentSkipListSet<String>(String.CASE_INSENSITIVE_ORDER),
		new CopyOnWriteArraySet<T>(),
		new ConcurrentSkipListMap<String, T>(String.CASE_INSENSITIVE_ORDER),
		new ConcurrentHashMap<T, String>()
		);
	}
	
	// -------------------------------------------- //
	// INTERFACE IMPLEMENTATION
	// -------------------------------------------- //
	
	@Override
	public abstract Class<T> getManagedClass();

	@Override
	public T createNewInstance()
	{
		try
		{
			return this.getManagedClass().newInstance();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public synchronized T create()
	{
		return this.create(null);
	}
	
	@Override
	public synchronized T create(Object oid)
	{
		T entity = this.createNewInstance();
		if (this.attach(entity, oid) == null) return null;
		return entity;
	}

	@Override
	public synchronized String attach(T entity)
	{
		return this.attach(entity, null);
	}
	
	@Override
	public synchronized String attach(T entity, Object oid)
	{
		return this.attach(entity, oid, false);
	}
	
	protected synchronized String attach(T entity, Object oid, boolean allowExistingIdUsage)
	{
		// Check entity
		if (entity == null) return null;
		String id = this.id(entity);
		if (id != null) return id;
		
		// Check/Fix id
		if (oid == null)
		{
			id = this.idNext(true);
		}
		else
		{
			id = this.idFix(oid);
			if (id == null) return null;
			if (this.containsId(id) && ! allowExistingIdUsage) return null;
		}
		
		// Attach
		this.ids.add(id);
		this.entities.add(entity);
		this.id2entity.put(id, entity);
		this.entity2id.put(entity, id);
		
		// Update Auto Increment
		this.idUpdateCurrentFor(id);
		return id;
	}
	
	@Override
	public synchronized void detachEntity(T entity)
	{
		if (entity == null) return;
		String id = this.entity2id.get(entity);
		if (id == null) return;
		this.detach(entity, id);
	}
	
	@Override
	public synchronized void detachId(Object oid)
	{
		String id = this.idFix(oid);
		if (id == null) return;
		T entity = this.id2entity.get(id);
		this.detach(entity, id);	
	}
	
	// Assumes the id is correct! For internal use only!
	protected synchronized void detach(T entity, String id)
	{
		if (id != null)
		{
			this.ids.remove(id);
			this.id2entity.remove(id);
			this.removeFile(id);
		}
		
		if (entity != null)
		{
			this.entities.remove(entity);
			this.entity2id.remove(entity);
		}
	}
	
	protected void removeFile(String id)
	{
		File file = this.fileFromId(id);
		if (file.exists())
		{
			file.delete();
		}
	}

	@Override
	public boolean containsEntity(T entity)
	{
		return this.entity2id.containsKey(entity);
	}
	
	@Override
	public boolean containsId(Object oid)
	{
		String id = this.idFix(oid);
		if (id == null) return false;
		return ids.contains(id);
	}
	
	@Override
	public boolean saveEntity(T entity)
	{
		String id = this.id(entity);
		return this.save(id, entity);
	}
	
	@Override
	public boolean saveId(Object oid)
	{
		String id = this.idFix(oid);
		T entity = this.get(id);
		return this.save(id, entity);
	}
	
	protected boolean save(String id, T entity)
	{
		if (id == null) return false;
		if (entity == null) return false;
		if (this.shouldBeSaved(entity))
		{
			String json = this.getGson().toJson(entity);
			File file = this.fileFromId(id);
			return DiscUtil.writeCatch(file, json);
		}
		this.removeFile(id);
		// TODO: Remove if loaded??
		
		return true;
		// TODO: Perhaps implement a logger in the interface?
	}

	@Override
	public boolean saveAll()
	{
		// Delete all files we do not care about.
		for (File file : this.getFolder().listFiles(JsonFileFilter.getInstance()))
		{
			String id = this.idFromFile(file);
			if ( ! this.containsId(id))
			{
				file.delete();
			}
		}
		
		// Save all loaded entites.
		for (Entry<String, T> entry: this.id2entity.entrySet())
		{
			this.save(entry.getKey(), entry.getValue());
		}
		// TODO: Bogus! This should return if a single error was encountered!
		return true;
	}

	@Override
	public boolean loaded(Object oid)
	{
		String id = this.idFix(oid);
		if (id == null) return false;
		return this.id2entity.containsKey(id);
	}
	
	@Override
	public synchronized T load(Object oid)
	{
		String id = this.idFix(oid);
		if (id == null) return null;
		T entity = this.id2entity.get(id);
		if (entity != null) return entity;
		if ( ! this.containsId(id)) return null;
		File file = this.fileFromId(id);
		String json = DiscUtil.readCatch(file);
		if (json == null) return null;
		entity = this.getGson().fromJson(json, this.getManagedClass());
		this.attach(entity, id, true);
		return entity;
	}
	
	@Override
	public boolean loadAll()
	{
		if (this.didLoadAll) return false;
		for (String id : this.ids)
		{
			this.load(id);
		}
		this.didLoadAll = true;
		return true;
	}

	@Override
	public boolean shouldBeSaved(T entity)
	{
		return true;
	}

	@Override
	public String id(T entity)
	{
		return this.entity2id.get(entity);
	}
	
	@Override
	public abstract String idFix(Object oid);
	
	@Override
	public abstract boolean idCanFix(Class<?> clazz);

	@Override
	public String idCurrent()
	{
		return Long.toString(this.idCurrent);
	}

	@Override
	public synchronized String idNext(boolean advance)
	{
		long next = this.idCurrent;
		String nextString = String.valueOf(next); 
		while (this.containsId(nextString))
		{
			next += 1;
			nextString = String.valueOf(next);
		}
		if (advance)
		{
			this.idCurrent = next;
		}
		return nextString;
	}
	
	@Override
	public synchronized boolean idUpdateCurrentFor(Object oid)
	{
		String id = this.idFix(oid);
		if (id == null) return false;
		long primid;
		try
		{
			primid = Long.parseLong(id);
		}
		catch (Exception e)
		{
			// The id was not a number. No need to care about it.
			return false;
		}
		if (this.idCurrent < primid)
		{
			this.idCurrent = primid;
			return true;
		}
		return false;
	}

	@Override
	public synchronized T get(Object oid, boolean creative)
	{
		String id = this.idFix(oid);
		if (id == null) return null;
		T ret = this.load(id);
		if (ret != null)
		{
			return ret;
		}
		if ( ! creative) return null;
		return this.create(id);
	}

	@Override
	public T get(Object oid)
	{
		return this.get(oid, this.getIsCreative());
	}

	@Override
	public Collection<T> getAllLoaded()
	{
		return entities;
	}
	
	@Override
	public Collection<T> getAllLoaded(Predictate<T> where)
	{
		return Persist.uglySQL(this.getAllLoaded(), where, null, null, null);
	}

	@Override
	public Collection<T> getAllLoaded(Predictate<T> where, Comparator<T> orderby)
	{
		return Persist.uglySQL(this.getAllLoaded(), where, orderby, null, null);
	}

	@Override
	public Collection<T> getAllLoaded(Predictate<T> where, Comparator<T> orderby, Integer limit)
	{
		return Persist.uglySQL(this.getAllLoaded(), where, orderby, limit, null);
	}

	@Override
	public Collection<T> getAllLoaded(Predictate<T> where, Comparator<T> orderby, Integer limit, Integer offset)
	{
		return Persist.uglySQL(this.getAllLoaded(), where, orderby, limit, offset);
	}
	
	@Override
	public Collection<T> getAll()
	{
		this.loadAll();
		return entities;
	}

	@Override
	public Collection<T> getAll(Predictate<T> where)
	{
		return Persist.uglySQL(this.getAll(), where, null, null, null);
	}

	@Override
	public Collection<T> getAll(Predictate<T> where, Comparator<T> orderby)
	{
		return Persist.uglySQL(this.getAll(), where, orderby, null, null);
	}

	@Override
	public Collection<T> getAll(Predictate<T> where, Comparator<T> orderby, Integer limit)
	{
		return Persist.uglySQL(this.getAll(), where, orderby, limit, null);
	}

	@Override
	public Collection<T> getAll(Predictate<T> where, Comparator<T> orderby, Integer limit, Integer offset)
	{
		return Persist.uglySQL(this.getAll(), where, orderby, limit, offset);
	}

	@Override
	public Collection<String> getIds()
	{
		return this.ids;
	}

	@Override
	public Map<String, T> getMap()
	{
		return this.id2entity;
	}

	@Override
	public T getBestMatch(Object oid)
	{
		String start = this.idFix(oid);
		if (start == null) return null;
		String id = Persist.getBestCIStart(this.ids, start);
		return this.get(id);
	}
}
