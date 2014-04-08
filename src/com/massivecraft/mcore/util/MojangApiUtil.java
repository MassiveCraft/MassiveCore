package com.massivecraft.mcore.util;

import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

/**
 * Many thanks to evilmidget38!
 * This utility class is based on his work.
 * http://forums.bukkit.org/threads/player-name-uuid-fetcher.250926/
 */
public class MojangApiUtil
{
	// -------------------------------------------- //
	// NAME --> ID
	// -------------------------------------------- //
	// The player names you supply does not have to use correct capitalization.
	// In the map returned however, the names will have correction capitalization.
	
	public static Map<String, UUID> getPlayerIds(Collection<String> playerNames) throws Exception
	{
		Map<String, UUID> ret = new HashMap<String, UUID>();
		JSONParser jsonParser = new JSONParser();
		String body = createBody(playerNames);
		for (int i = 1; i < 100; i++)
		{
			HttpURLConnection connection = createConnection(i);
			writeBody(connection, body);
			JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(connection.getInputStream()));
			JSONArray profiles = (JSONArray) jsonObject.get("profiles");
			Number count = (Number) jsonObject.get("size");
			
			if (count.intValue() == 0)
			{
				break;
			}
			
			for (Object profile : profiles)
			{
				JSONObject jsonProfile = (JSONObject) profile;
				String id = (String) jsonProfile.get("id");
				String name = (String) jsonProfile.get("name");
				UUID uuid = UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" + id.substring(20, 32));
				ret.put(name, uuid);
			}
		}
		return ret;
	}
	
	private static void writeBody(HttpURLConnection connection, String body) throws Exception
	{
		DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
		writer.write(body.getBytes());
		writer.flush();
		writer.close();
	}
	
	private static HttpURLConnection createConnection(int page) throws Exception
	{
		URL url = new URL("https://api.mojang.com/profiles/page/" + page);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json");
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
			obj.put("name", playerName);
			obj.put("agent", "minecraft");
			lookups.add(obj);
		}
		return JSONValue.toJSONString(lookups);
	}
	
	// -------------------------------------------- //
	// ID --> NAME 
	// -------------------------------------------- //
	
	public static Map<UUID, String> getPlayerNames(Collection<UUID> playerIds) throws Exception
	{
		Map<UUID, String> ret = new HashMap<UUID, String>();
		JSONParser jsonParser = new JSONParser();
		for (UUID playerId: playerIds)
		{
			HttpURLConnection connection = (HttpURLConnection) new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + playerId.toString().replace("-", "")).openConnection();
			JSONObject response = (JSONObject) jsonParser.parse(new InputStreamReader(connection.getInputStream()));
			String name = (String) response.get("name");
			if (name == null)
			{
				continue;
			}
			String cause = (String) response.get("cause");
			if (cause != null && cause.length() > 0)
			{
				String errorMessage = (String) response.get("errorMessage");
				throw new IllegalStateException(errorMessage);
			}
			ret.put(playerId, name);
		}
		return ret;
	}
	
}
