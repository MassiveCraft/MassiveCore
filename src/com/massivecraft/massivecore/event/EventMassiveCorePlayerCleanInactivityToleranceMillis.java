package com.massivecraft.massivecore.event;

import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.store.SenderColl;
import com.massivecraft.massivecore.store.SenderEntity;
import org.bukkit.event.HandlerList;

import java.util.LinkedHashMap;
import java.util.Map;

public class EventMassiveCorePlayerCleanInactivityToleranceMillis extends EventMassiveCore
{
	// -------------------------------------------- //
	// REQUIRED EVENT CODE
	// -------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	@Override public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }
	
	// -------------------------------------------- //
	// FIELD
	// -------------------------------------------- //
	
	private final long lastActivityMillis;
	public long getLastActivityMillis() { return lastActivityMillis; }
	
	private final long now;
	public long getNow() { return now; }
	
	private final SenderEntity<?> entity;
	public SenderEntity<?> getEntity() { return this.entity; }
	
	public SenderColl<?> getColl() { return entity.getColl(); }
	
	private final Map<String, Long> toleranceCauseMillis = new MassiveMap<>();
	public Map<String, Long> getToleranceCauseMillis() { return this.toleranceCauseMillis; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public EventMassiveCorePlayerCleanInactivityToleranceMillis(long lastActivityMillis, SenderEntity entity)
	{
		this(lastActivityMillis, System.currentTimeMillis(), entity);
	}
	
	public EventMassiveCorePlayerCleanInactivityToleranceMillis(long lastActivityMillis, long now, SenderEntity entity)
	{
		this.lastActivityMillis = lastActivityMillis;
		this.now = now;
		this.entity = entity;
	}
	
	// -------------------------------------------- //
	// CONVENIENCE
	// -------------------------------------------- //
	
	public long getToleranceMillis()
	{
		long ret = 0;
		
		for (Long value : toleranceCauseMillis.values())
		{
			ret += value;
		}
		
		return ret;
	}
	
	public boolean shouldBeCleaned()
	{
		long toleranceMillis = getToleranceMillis();
		
		long now = this.getNow();
		long lastActivityMillis = this.getLastActivityMillis();
		
		// If unknown don't remove
		if (lastActivityMillis <= 0) return false;
		
		long removeTime = lastActivityMillis + toleranceMillis;
		
		return now >= removeTime;
	}
	
}
