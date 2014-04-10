package com.massivecraft.mcore.fetcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import com.massivecraft.mcore.MCoreMPlayer;

/**
 * Many thanks to evilmidget38!
 * This utility class is based on his work.
 * http://forums.bukkit.org/threads/player-name-uuid-fetcher.250926/
 */
public class FetcherPlayerIdCached implements Callable<Map<String, UUID>>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final Collection<String> playerNames;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public FetcherPlayerIdCached(Collection<String> playerNames)
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
		List<String> playerNamesCopy = new ArrayList<String>(playerNames);
		
		// Use Cache
		Iterator<String> iter = playerNamesCopy.iterator();
		while (iter.hasNext())
		{
			String playerName = iter.next();
			MCoreMPlayer mplayer = MCoreMPlayer.get(playerName);
			if (mplayer == null) continue;
			ret.put(mplayer.getName(), UUID.fromString(mplayer.getId()));
			iter.remove();
		}	
		
		// Use Mojang API for the rest
		if (playerNamesCopy.size() > 0)
		{
			try
			{
				Map<String, UUID> mojangApiResult = FetcherPlayerIdMojang.fetch(playerNamesCopy);
				// Add to the cache
				for (Entry<String, UUID> entry : mojangApiResult.entrySet())
				{
					String name = entry.getKey();
					UUID id = entry.getValue();
					MCoreMPlayer mplayer = MCoreMPlayer.get(id, true);
					mplayer.setName(name);
				}
				// Add to the return value
				ret.putAll(mojangApiResult);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		// Return
		return ret;
	}
	
}
