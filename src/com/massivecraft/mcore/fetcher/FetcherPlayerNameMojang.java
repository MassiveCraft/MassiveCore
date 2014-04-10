package com.massivecraft.mcore.fetcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Many thanks to evilmidget38!
 * This utility class is based on his work.
 * http://forums.bukkit.org/threads/player-name-uuid-fetcher.250926/
 */
public class FetcherPlayerNameMojang implements Callable<Map<UUID, String>>
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final ExecutorService ES = Executors.newCachedThreadPool();
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final Collection<UUID> playerIds;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public FetcherPlayerNameMojang(Collection<UUID> playerIds)
	{
		this.playerIds = playerIds;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public Map<UUID, String> call() throws Exception
	{
		return fetch(this.playerIds);
	}
	
	// -------------------------------------------- //
	// STATIC
	// -------------------------------------------- //
	
	public static Map<UUID, String> fetch(Collection<UUID> playerIds) throws Exception
	{
		// Create Tasks
		final List<Callable<Entry<UUID, String>>> tasks = new ArrayList<Callable<Entry<UUID, String>>>();
		for (UUID playerId : playerIds)
		{
			tasks.add(new FetcherPlayerNameMojangSingle(playerId));
		}
		
		// Invoke All Tasks
		List<Future<Entry<UUID, String>>> futures = ES.invokeAll(tasks);
		
		// Merge Return Value
		Map<UUID, String> ret = new HashMap<UUID, String>();
		for (Future<Entry<UUID, String>> future : futures)
		{
			Entry<UUID, String> entry = future.get();
			if (entry == null) continue;
			ret.put(entry.getKey(), entry.getValue());
		}
		
		return ret;
	}
	
}
