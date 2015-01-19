package com.massivecraft.massivecore.fetcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FetcherByName implements Callable<Map<String, IdAndName>>
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final ExecutorService ES = Executors.newFixedThreadPool(100);
	public static final int BATCH_SIZE = FetcherByNameSingle.PROFILES_PER_REQUEST;
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final Collection<String> names;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public FetcherByName(Collection<String> names)
	{
		this.names = names;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public Map<String, IdAndName> call() throws Exception
	{
		return fetch(this.names);
	}
	
	// -------------------------------------------- //
	// STATIC
	// -------------------------------------------- //
	
	public static Map<String, IdAndName> fetch(Collection<String> names) throws Exception
	{
		// Create batches
		List<List<String>> batches = new ArrayList<List<String>>();
		names = new ArrayList<String>(names);
		while (names.size() > 0)
		{
			List<String> batch = take(names, BATCH_SIZE);
			batches.add(batch);
		}
		
		// Create Tasks
		final List<Callable<Map<String, IdAndName>>> tasks = new ArrayList<Callable<Map<String, IdAndName>>>();
		for (List<String> batch : batches)
		{
			tasks.add(new FetcherByNameSingle(batch));
		}
		
		// Invoke All Tasks
		List<Future<Map<String, IdAndName>>> futures = ES.invokeAll(tasks);
		
		// Merge Return Value
		Map<String, IdAndName> ret = new TreeMap<String, IdAndName> (String.CASE_INSENSITIVE_ORDER);
		for (Future<Map<String, IdAndName>> future : futures)
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
