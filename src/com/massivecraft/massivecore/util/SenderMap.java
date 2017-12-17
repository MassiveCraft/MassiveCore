package com.massivecraft.massivecore.util;

import com.massivecraft.massivecore.SenderPresence;
import com.massivecraft.massivecore.SenderType;
import com.massivecraft.massivecore.xlib.guava.collect.ImmutableList;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public final class SenderMap
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final Map<SenderPresence, Map<SenderType, Set<String>>> innerMap;
	
	public Map<SenderPresence, Map<SenderType, Set<String>>> getInnerMap()
	{
		return innerMap;
	}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public SenderMap()
	{
		innerMap = new EnumMap<>(SenderPresence.class);
		for (SenderPresence presence : SenderPresence.values())
		{
			Map<SenderType, Set<String>> map = new EnumMap<>(SenderType.class);
			for (SenderType type : SenderType.values())
			{
				Set<String> set = new ConcurrentSkipListSet<>(String.CASE_INSENSITIVE_ORDER);
				map.put(type, set);
			}
			innerMap.put(presence, map);
		}
	}

	// -------------------------------------------- //
	// GET
	// -------------------------------------------- //
	
	public Set<String> getValues(SenderPresence presence, SenderType type)
	{
		if (presence == null) throw new NullPointerException("presence");
		if (type == null) throw new NullPointerException("type");
	
		return Collections.unmodifiableSet(getRawValues(presence, type));
	}
	
	private Set<String> getRawValues(SenderPresence presence, SenderType type)
	{
		return innerMap.get(presence).get(type);
	}
	
	public SenderPresence getPresence(String value)
	{
		if (value == null) throw new NullPointerException("value");
		return getPresence(value, SenderType.ANY);
	}
	
	public SenderPresence getPresence(String value, SenderType type)
	{
		if (value == null) throw new NullPointerException("value");
		if (type == null) throw new NullPointerException("type");
		
		
		for (SenderPresence presence : SenderPresence.values())
		{
			if (contains(value, presence, type)) return presence;
		}

		return null;
	}
	
	// -------------------------------------------- //
	// CONTAINS
	// -------------------------------------------- //
	
	public boolean contains(String value, SenderPresence presence, SenderType type)
	{
		if (value == null) throw new NullPointerException("value");
		if (presence == null) throw new NullPointerException("presence");
		if (type == null) throw new NullPointerException("type");
		
		return getRawValues(presence, type).contains(value);
	}
	
	// -------------------------------------------- //
	// CLEAR
	// -------------------------------------------- //
	
	public void clear()
	{
		for (Map<SenderType, Set<String>> map : innerMap.values())
		{
			for (Set<String> set : map.values())
			{
				set.clear();
			}
		}
	}
	
	// -------------------------------------------- //
	// ADD
	// -------------------------------------------- //
	
	public void addValue(String value, SenderPresence presence)
	{
		if (value == null) throw new NullPointerException("value");
		if (presence == null) throw new NullPointerException("presence");
		
		addValue(value, getPresences(presence));
	}
	
	public void addValue(String value, List<SenderPresence> presences)
	{
		if (value == null) throw new NullPointerException("value");
		if (presences == null) throw new NullPointerException("presences");
		
		addValue(value, presences, getSenderTypes(value));
	}
	
	public void addValue(String value, List<SenderPresence> presences, List<SenderType> types)
	{
		if (value == null) throw new NullPointerException("value");
		if (presences == null) throw new NullPointerException("presences");
		if (types == null) throw new NullPointerException("types");
		
		for (SenderPresence presence : presences)
		{
			Map<SenderType, Set<String>> map = innerMap.get(presence);
			for (SenderType type : types)
			{
				Set<String> set = map.get(type);
				set.add(value);
			}
		}
	}
	
	// -------------------------------------------- //
	// REMOVE
	// -------------------------------------------- //
	
	public boolean removeValueCompletely(String value)
	{
		if (value == null) throw new NullPointerException("value");

		boolean ret = false;
		for (Map<SenderType, Set<String>> map : innerMap.values())
		{
			for (Set<String> set : map.values())
			{
				ret |= set.remove(value);
			}
		}
		return ret;
	}

	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static final List<SenderPresence> LOCAL_PRESENCES = ImmutableList.of(SenderPresence.LOCAL, SenderPresence.ONLINE, SenderPresence.ANY);
	public static final List<SenderPresence> ONLINE_PRESENCES = ImmutableList.of(SenderPresence.ONLINE, SenderPresence.ANY);
	public static final List<SenderPresence> OFFLINE_PRESENCES = ImmutableList.of(SenderPresence.OFFLINE, SenderPresence.ANY);
	
	// This accepts the most strict presence,
	// and returns all other which also match.
	public static List<SenderPresence> getPresences(SenderPresence presence)
	{
		if (presence == null) throw new NullPointerException("presence");
		switch (presence)
		{
			case LOCAL : return LOCAL_PRESENCES;
			case ONLINE : return ONLINE_PRESENCES;
			case OFFLINE : return OFFLINE_PRESENCES;
			case ANY : throw new UnsupportedOperationException("SenderPresence.ANY is not supported. You must know wether it is online or offline.");
		}
		throw new UnsupportedOperationException("Unknown SenderPresence: " + null);
	}
	
	public static final List<SenderType> PLAYER_TYPES = ImmutableList.of(SenderType.PLAYER, SenderType.ANY);
	public static final List<SenderType> NONPLAYER_TYPES = ImmutableList.of(SenderType.NONPLAYER, SenderType.ANY);
	
	public static List<SenderType> getSenderTypes(String value)
	{
		if (value == null) throw new NullPointerException("value");
		if (isPlayerValue(value)) return PLAYER_TYPES;
		else return NONPLAYER_TYPES;
	}
	
	public static boolean isPlayerValue(String value)
	{
		return IdUtil.isPlayerId(value);
		//return MUtil.isValidPlayerName(value) || MUtil.isUuid(value);
	}
	
}
