package com.massivecraft.mcore.fetcher;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;
import java.util.UUID;
import java.util.concurrent.Callable;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Many thanks to evilmidget38!
 * This utility class is based on his work.
 * http://forums.bukkit.org/threads/player-name-uuid-fetcher.250926/
 */
public class FetcherPlayerNameMojangSingle implements Callable<Entry<UUID, String>>
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public final static String URL_BASE = "https://sessionserver.mojang.com/session/minecraft/profile/";
	public final static String KEY_NAME = "name";
	public final static String KEY_CAUSE = "cause";
	public final static String KEY_ERROR_MESSAGE = "errorMessage";
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final UUID playerId;
	public UUID getPlayerId() { return this.playerId; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public FetcherPlayerNameMojangSingle(UUID playerId)
	{
		this.playerId = playerId;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public Entry<UUID, String> call() throws Exception
	{
		String playerName = fetch(this.playerId);
		if (playerName == null) return null;
		return new SimpleEntry<UUID, String>(this.playerId, playerName);
	}
	
	// -------------------------------------------- //
	// STATIC
	// -------------------------------------------- //
	
	public static String fetch(UUID playerId) throws Exception
	{
		JSONParser jsonParser = new JSONParser();
		HttpURLConnection connection = createConnection(playerId);
		JSONObject response = (JSONObject) jsonParser.parse(new InputStreamReader(connection.getInputStream()));
		String name = (String) response.get(KEY_NAME);
		if (name == null) return null;
		String cause = (String) response.get(KEY_CAUSE);
		if (cause != null && cause.length() > 0)
		{
			String errorMessage = (String) response.get(KEY_ERROR_MESSAGE);
			throw new IllegalStateException(errorMessage);
		}
		return name;
	}
	
	private static HttpURLConnection createConnection(UUID playerId) throws Exception
	{
		URL url = new URL(URL_BASE + playerId.toString().replace("-", ""));
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(15000);
		connection.setReadTimeout(15000);
		connection.setUseCaches(false);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		return connection;
	}
	
}
