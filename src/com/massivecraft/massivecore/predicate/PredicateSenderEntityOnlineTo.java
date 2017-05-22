package com.massivecraft.massivecore.predicate;

import com.massivecraft.massivecore.store.SenderEntity;
import com.massivecraft.massivecore.util.IdUtil;

public class PredicateSenderEntityOnlineTo implements Predicate<SenderEntity>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final String watcherId;
	public String getWatcherId() { return this.watcherId; }
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static PredicateSenderEntityOnlineTo get(Object watcherObject) { return new PredicateSenderEntityOnlineTo(watcherObject); }
	public PredicateSenderEntityOnlineTo(Object watcherObject)
	{
		this.watcherId = IdUtil.getId(watcherObject);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean apply(SenderEntity watchee)
	{
		return watchee.isOnline(this.getWatcherId());
	}

}
