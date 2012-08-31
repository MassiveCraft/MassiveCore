package com.massivecraft.mcore4.store;

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

import com.massivecraft.mcore4.lib.gson.JsonElement;
import com.massivecraft.mcore4.lib.gson.JsonParser;
import com.massivecraft.mcore4.store.idstrategy.IdStrategyAiGson;
import com.massivecraft.mcore4.store.idstrategy.IdStrategyOidGson;
import com.massivecraft.mcore4.store.idstrategy.IdStrategyUuidMongoAndGson;
import com.massivecraft.mcore4.store.storeadapter.StoreAdapter;
import com.massivecraft.mcore4.store.storeadapter.StoreAdapterGson;
import com.massivecraft.mcore4.util.DiscUtil;

public class DriverGson extends DriverAbstract<JsonElement>
{
	protected final static String DOTJSON = ".json";
	
	// -------------------------------------------- //
	// IMPLEMENTATION
	// -------------------------------------------- //
	
	@Override public Class<JsonElement> getRawdataClass() { return JsonElement.class; }
	
	@Override
	public StoreAdapter getStoreAdapter()
	{
		return StoreAdapterGson.get();
	}
		
	@Override
	public Db<JsonElement> db(String uri)
	{
		// "gson://" is 7 chars
		File folder = new File(uri.substring(7));
		folder.mkdirs();
		return new DbGson(this, folder);
	}

	@Override
	public Set<String> collnames(Db<?> db)
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
	public <L> boolean containsId(Coll<?, L> coll, L id)
	{
		return fileFromId(coll, id).isFile();
	}
	
	@Override
	public <L> Long mtime(Coll<?, L> coll, L id)
	{
		File file = fileFromId(coll, id);
		if ( ! file.isFile()) return null;
		return file.lastModified();
	}
	
	@Override
	public <L> Collection<L> ids(Coll<?, L> coll)
	{
		List<L> ret = new ArrayList<L>();
		
		// Scan the collection folder for .json files
		File collDir = getCollDir(coll);
		if ( ! collDir.isDirectory()) return ret;
		for(File file : collDir.listFiles(JsonFileFilter.get()))
		{
			// Then convert them to what they should be
			String remoteId = idFromFile(file);
			L localId = coll.idStrategy().remoteToLocal(remoteId);
			ret.add(localId);
		}
		
		return ret;
	}
	
	@Override
	public <L> Map<L, Long> id2mtime(Coll<?, L> coll)
	{
		Map<L, Long> ret = new HashMap<L, Long>();
		
		// Scan the collection folder for .json files
		File collDir = getCollDir(coll);
		if ( ! collDir.isDirectory()) return ret;
		for(File file : collDir.listFiles(JsonFileFilter.get()))
		{
			// Then convert them to what they should be
			String remoteId = idFromFile(file);
			L localId = coll.idStrategy().remoteToLocal(remoteId);
			ret.put(localId, file.lastModified());
		}
		
		return ret;
	}
	
	@Override
	public <L> Entry<JsonElement, Long> load(Coll<?, L> coll, L id)
	{
		File file = fileFromId(coll, id);
		Long mtime = file.lastModified();
		if (mtime == 0) return null;
		String content = DiscUtil.readCatch(file);
		if (content == null) return null;
		JsonElement raw = new JsonParser().parse(content);
		return new SimpleEntry<JsonElement, Long>(raw, mtime);
	}

	@Override
	public <L> Long save(Coll<?, L> coll, L id, Object rawData)
	{
		File file = fileFromId(coll, id);
		getCollDir(coll).mkdirs();
		String content = coll.mplugin().gson.toJson((JsonElement)rawData);
		if (DiscUtil.writeCatch(file, content) == false) return null;
		return file.lastModified();
	}

	@Override
	public <L> void delete(Coll<?, L> coll, L id)
	{
		File file = fileFromId(coll, id);
		file.delete();
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	protected static File getCollDir(Coll<?, ?> coll)
	{
		return (File) coll.collDriverObject();
	}
	
	protected static String idFromFile(File file)
	{
		if (file == null) return null;
		String name = file.getName();
		return name.substring(0, name.length()-5);
	}
	
	protected static <L> File fileFromId(Coll<?, L> coll, L id)
	{
		File collDir = getCollDir(coll);
		String idString = (String)coll.idStrategy().localToRemote(id);
		File idFile = new File(collDir, idString+DOTJSON);
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
		instance.registerIdStrategy(IdStrategyAiGson.get());
		instance.registerIdStrategy(IdStrategyOidGson.get());
		instance.registerIdStrategy(IdStrategyUuidMongoAndGson.get());
	}

}
