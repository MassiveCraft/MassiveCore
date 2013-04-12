package com.massivecraft.mcore.store;

import java.io.File;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.massivecraft.mcore.store.idstrategy.IdStrategyOid;
import com.massivecraft.mcore.store.idstrategy.IdStrategyUuid;
import com.massivecraft.mcore.store.storeadapter.StoreAdapter;
import com.massivecraft.mcore.store.storeadapter.StoreAdapterGson;
import com.massivecraft.mcore.util.DiscUtil;
import com.massivecraft.mcore.xlib.gson.JsonElement;
import com.massivecraft.mcore.xlib.gson.JsonParser;

public class DriverGson extends DriverAbstract<JsonElement>
{
	protected final static String DOTJSON = ".json";
	
	// -------------------------------------------- //
	// IMPLEMENTATION
	// -------------------------------------------- //
	
	@Override public Class<JsonElement> getRawdataClass() { return JsonElement.class; }
	
	@Override
	public boolean equal(Object rawOne, Object rawTwo)
	{
		JsonElement one = (JsonElement)rawOne;
		JsonElement two = (JsonElement)rawTwo;
		
		if (one == null && two == null) return true;
		if (one == null || two == null) return false;
		
		return one.toString().equals(two.toString());
	}
	
	@Override
	public StoreAdapter getStoreAdapter()
	{
		return StoreAdapterGson.get();
	}
		
	@Override
	public Db<JsonElement> getDb(String uri)
	{
		// "gson://" is 7 chars
		File folder = new File(uri.substring(7));
		folder.mkdirs();
		return new DbGson(this, folder);
	}

	@Override
	public Set<String> getCollnames(Db<?> db)
	{
		Set<String> ret = new LinkedHashSet<String>();
		
		for (File f : ((DbGson)db).dir.listFiles())
		{
			if ( ! f.isDirectory()) continue;
			ret.add(f.getName());
		}
	
		return ret;
	}

	@Override
	public boolean containsId(Coll<?> coll, String id)
	{
		return fileFromId(coll, id).isFile();
	}
	
	@Override
	public Long getMtime(Coll<?> coll, String id)
	{
		File file = fileFromId(coll, id);
		if ( ! file.isFile()) return null;
		return file.lastModified();
	}
	
	@Override
	public Collection<String> getIds(Coll<?> coll)
	{
		List<String> ret = new ArrayList<String>();
		
		// Scan the collection folder for .json files
		File collDir = getCollDir(coll);
		if ( ! collDir.isDirectory()) return ret;
		for(File file : collDir.listFiles(JsonFileFilter.get()))
		{
			ret.add(idFromFile(file));
		}
		
		return ret;
	}
	
	@Override
	public Map<String, Long> getId2mtime(Coll<?> coll)
	{
		Map<String, Long> ret = new HashMap<String, Long>();
		
		// Scan the collection folder for .json files
		File collDir = getCollDir(coll);
		if ( ! collDir.isDirectory()) return ret;
		for(File file : collDir.listFiles(JsonFileFilter.get()))
		{
			ret.put(idFromFile(file), file.lastModified());
		}
		
		return ret;
	}
	
	@Override
	public Entry<JsonElement, Long> load(Coll<?> coll, String id)
	{
		File file = fileFromId(coll, id);
		Long mtime = file.lastModified();
		if (mtime == 0) return null;
		String content = DiscUtil.readCatch(file);
		if (content == null) return null;
		if (content.length() == 0) return null;
		JsonElement raw = new JsonParser().parse(content);
		return new SimpleEntry<JsonElement, Long>(raw, mtime);
	}

	@Override
	public Long save(Coll<?> coll, String id, Object rawData)
	{
		File file = fileFromId(coll, id);
		String content = coll.getGson().toJson((JsonElement)rawData);
		if (DiscUtil.writeCatch(file, content) == false) return null;
		return file.lastModified();
	}

	@Override
	public void delete(Coll<?> coll, String id)
	{
		File file = fileFromId(coll, id);
		file.delete();
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	protected static File getCollDir(Coll<?> coll)
	{
		return (File) coll.getCollDriverObject();
	}
	
	protected static String idFromFile(File file)
	{
		if (file == null) return null;
		String name = file.getName();
		return name.substring(0, name.length()-5);
	}
	
	protected static File fileFromId(Coll<?> coll, String id)
	{
		File collDir = getCollDir(coll);
		File idFile = new File(collDir, id+DOTJSON);
		return idFile;
	}
	
	//----------------------------------------------//
	// CONSTRUCTORS
	//----------------------------------------------//
	
	public static String NAME = "gson";
	
	private DriverGson()
	{
		super(NAME);
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	protected static DriverGson instance;
	public static DriverGson get()
	{
		return instance;
	}
	
	static
	{
		instance = new DriverGson();
		instance.registerIdStrategy(IdStrategyOid.get());
		instance.registerIdStrategy(IdStrategyUuid.get());
	}

}
