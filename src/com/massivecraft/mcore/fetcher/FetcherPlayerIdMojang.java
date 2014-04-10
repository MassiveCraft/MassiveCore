package com.massivecraft.mcore.fetcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
public class FetcherPlayerIdMojang implements Callable<Map<String, UUID>>
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final int BATCH_SIZE = FetcherPlayerIdMojangSingle.MAX_PAGE_SIZE;
	public static final ExecutorService ES = Executors.newCachedThreadPool();
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final Collection<String> playerNames;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public FetcherPlayerIdMojang(Collection<String> playerNames)
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
		// Create batches
		List<List<String>> batches = new ArrayList<List<String>>();
		playerNames = new ArrayList<String>(playerNames);
		while (playerNames.size() > 0)
		{
			List<String> batch = take(playerNames, BATCH_SIZE);
			batches.add(batch);
		}
		
		// Create Tasks
		final List<Callable<Map<String, UUID>>> tasks = new ArrayList<Callable<Map<String, UUID>>>();
		for (List<String> batch : batches)
		{
			tasks.add(new FetcherPlayerIdMojangSingle(batch));
		}
		
		// Invoke All Tasks
		List<Future<Map<String, UUID>>> futures = ES.invokeAll(tasks);
		
		// Merge Return Value
		Map<String, UUID> ret = new TreeMap<String, UUID> (String.CASE_INSENSITIVE_ORDER);
		for (Future<Map<String, UUID>> future : futures)
		{
			
			ret.putAll(future.get());
		}
		
		return ret;
	}
	
	public static <T> List<T> take(Collection<T> coll, int count)
	{
		List<T> ret = new ArrayList<T>();
		
		Iterator<T> iter = coll.iterator();
		int i = 0;
		while (iter.hasNext() && i < count)
		{
			i++;
			ret.add(iter.next());
			iter.remove();
		}
		
		return ret;
	}
	
}
