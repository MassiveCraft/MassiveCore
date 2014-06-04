package com.massivecraft.massivecore.fetcher;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Many thanks to evilmidget38!
 * This utility class is based on his work.
 * http://forums.bukkit.org/threads/player-name-uuid-fetcher.250926/
 */
public class FetcherByIdSingle implements Callable<Map<UUID, IdAndName>>
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public final static String PROFILE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/";
	public final static String KEY_NAME = "name";
	public final static String KEY_CAUSE = "cause";
	public final static String KEY_ERROR_MESSAGE = "errorMessage";
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final Collection<UUID> ids;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public FetcherByIdSingle(Collection<UUID> ids)
	{
		this.ids = ids;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public Map<UUID, IdAndName> call() throws Exception
	{
		return fetch(this.ids);
	}
	
	// -------------------------------------------- //
	// STATIC
	// -------------------------------------------- //
	
	public static Map<UUID, IdAndName> fetch(Collection<UUID> ids) throws Exception
	{
		Map<UUID, IdAndName> ret = new HashMap<UUID, IdAndName>();
		JSONParser jsonParser = new JSONParser();
		
		for (UUID id : ids)
		{
			HttpURLConnection connection = createConnection(id);
			InputStream inputStream = connection.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			
			JSONObject response = (JSONObject) jsonParser.parse(inputStreamReader);
			
			inputStreamReader.close();
			inputStream.close();
			connection.disconnect();
			
			String name = (String) response.get(KEY_NAME);
			// if (name == null) continue;
			// No... we want to add null values as well.
			
			String cause = (String) response.get(KEY_CAUSE);
			if (cause != null && cause.length() > 0)
			{
				String errorMessage = (String) response.get(KEY_ERROR_MESSAGE);
				throw new IllegalStateException(errorMessage);
			}
			
			ret.put(id, new IdAndName(id, name));
		}
		
		return ret;
	}
	
	private static HttpURLConnection createConnection(UUID id) throws Exception
	{
		URL url = new URL(PROFILE_URL + id.toString().replace("-", ""));
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(15000);
		connection.setReadTimeout(15000);
		connection.setUseCaches(false);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		return connection;
	}
	
}
