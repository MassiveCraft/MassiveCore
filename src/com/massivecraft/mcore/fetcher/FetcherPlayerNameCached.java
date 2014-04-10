package com.massivecraft.mcore.fetcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import com.massivecraft.mcore.MCoreMPlayer;

/**
 * Many thanks to evilmidget38!
 * This utility class is based on his work.
 * http://forums.bukkit.org/threads/player-name-uuid-fetcher.250926/
 */
public class FetcherPlayerNameCached implements Callable<Map<UUID, String>>
{	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final Collection<UUID> playerIds;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public FetcherPlayerNameCached(Collection<UUID> playerIds)
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
		Map<UUID, String> ret = new HashMap<UUID, String>();
		List<UUID> playerIdsCopy = new ArrayList<UUID>(playerIds);
		
		// Use Cache
		Iterator<UUID> iter = playerIdsCopy.iterator();
		while (iter.hasNext())
		{
			UUID playerId = iter.next();
			MCoreMPlayer mplayer = MCoreMPlayer.get(playerId);
			if (mplayer == null) continue;
			ret.put(playerId, mplayer.getName());
			iter.remove();
		}	
				
		// Use Mojang API for the rest
		if (playerIdsCopy.size() > 0)
		{
			try
			{
				Map<UUID, String> mojangApiResult = FetcherPlayerNameMojang.fetch(playerIdsCopy);
				// Add to the cache
				for (Entry<UUID, String> entry : mojangApiResult.entrySet())
				{
					UUID id = entry.getKey();
					String name = entry.getValue();
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
