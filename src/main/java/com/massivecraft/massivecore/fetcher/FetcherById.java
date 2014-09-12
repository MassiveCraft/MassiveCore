package com.massivecraft.massivecore.fetcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FetcherById implements Callable<Map<Object, IdAndName>>
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final ExecutorService ES = Executors.newFixedThreadPool(100);
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final Collection<UUID> ids;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public FetcherById(Collection<UUID> ids)
	{
		this.ids = ids;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public Map<Object, IdAndName> call() throws Exception
	{
		return fetch(this.ids);
	}
	
	// -------------------------------------------- //
	// STATIC
	// -------------------------------------------- //
	
	public static Map<Object, IdAndName> fetch(Collection<UUID> ids) throws Exception
	{
		// Create Tasks
		final List<Callable<Map<UUID, IdAndName>>> tasks = new ArrayList<Callable<Map<UUID, IdAndName>>>();
		for (UUID id : ids)
		{
			tasks.add(new FetcherByIdSingle(Arrays.asList(id)));
		}
		
		// Invoke All Tasks
		List<Future<Map<UUID, IdAndName>>> futures = ES.invokeAll(tasks);
		
		// Merge Return Value
		Map<Object, IdAndName> ret = new HashMap<Object, IdAndName>();
		for (Future<Map<UUID, IdAndName>> future : futures)
		{
			Map<UUID, IdAndName> map = future.get();
			ret.putAll(map);
		}
		
		return ret;
	}
	
}
