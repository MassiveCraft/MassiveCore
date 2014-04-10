package com.massivecraft.mcore.fetcher;

import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.Callable;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

/**
 * Many thanks to evilmidget38!
 * This utility class is based on his work.
 * http://forums.bukkit.org/threads/player-name-uuid-fetcher.250926/
 */
public class FetcherPlayerIdMojangSingle implements Callable<Map<String, UUID>>
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public final static String URL_BASE = "https://api.mojang.com/profiles/page/";
	public final static int MAX_PAGES = 100;
	
	// The maximum amount of profiles returned per page.
	// Mojang might change this value.
	// Thus we can not fully depend on it.
	public final static int MAX_PAGE_SIZE = 50;
	
	public final static String KEY_PROFILES = "profiles";
	public final static String KEY_SIZE = "size";
	public final static String KEY_ID = "id";
	public final static String KEY_NAME = "name";
	public final static String KEY_AGENT = "agent";
	public final static String VALUGE_AGENT = "minecraft";
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final Collection<String> playerNames;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public FetcherPlayerIdMojangSingle(Collection<String> playerNames)
	{
		this.playerNames = playerNames;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public Map<String, UUID> call() throws Exception
	{
		return fetch(this.playerNames);
	}
	
	// -------------------------------------------- //
	// STATIC
	// -------------------------------------------- //
	
	public static Map<String, UUID> fetch(Collection<String> playerNames) throws Exception
	{
		Map<String, UUID> ret = new TreeMap<String, UUID>(String.CASE_INSENSITIVE_ORDER);
		JSONParser jsonParser = new JSONParser();
		String body = createBody(playerNames);
		for (int i = 1; i < MAX_PAGES; i++)
		{
			// If the return object has as many entries as player names requested we must have gotten all the info.
			// This will often help us avoid the extra useless last call of a page with 0 entries.
			if (ret.size() == playerNames.size()) break;
			
			HttpURLConnection connection = createConnection(i);
			writeBody(connection, body);
			JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(connection.getInputStream()));
			JSONArray profiles = (JSONArray) jsonObject.get(KEY_PROFILES);
			int size = ((Number) jsonObject.get(KEY_SIZE)).intValue();
			
			// If the page is empty we are done
			if (size == 0) break;
			
			for (Object profile : profiles)
			{
				JSONObject jsonProfile = (JSONObject) profile;
				String id = (String) jsonProfile.get(KEY_ID);
				String name = (String) jsonProfile.get(KEY_NAME);
				UUID uuid = UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" + id.substring(20, 32));
				ret.put(name, uuid);
			}
		}
		return ret;
	}
	
	private static HttpURLConnection createConnection(int page) throws Exception
	{
		URL url = new URL(URL_BASE + page);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setConnectTimeout(15000);
		connection.setReadTimeout(15000);
		connection.setUseCaches(false);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		return connection;
	}
	
	@SuppressWarnings("unchecked")
	private static String createBody(Collection<String> playerNames)
	{
		List<JSONObject> lookups = new ArrayList<JSONObject>();
		for (String playerName : playerNames)
		{
			JSONObject obj = new JSONObject();
			obj.put(KEY_NAME, playerName);
			obj.put(KEY_AGENT, VALUGE_AGENT);
			lookups.add(obj);
		}
		return JSONValue.toJSONString(lookups);
	}
	
	private static void writeBody(HttpURLConnection connection, String body) throws Exception
	{
		DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
		writer.write(body.getBytes());
		writer.flush();
		writer.close();
	}
	
}
