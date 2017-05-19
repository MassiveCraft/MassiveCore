package com.massivecraft.massivecore.event;

import com.massivecraft.massivecore.store.SenderColl;
import com.massivecraft.massivecore.store.SenderEntity;
import com.massivecraft.massivecore.store.inactive.Inactive;
import org.bukkit.event.HandlerList;

import java.util.LinkedHashMap;
import java.util.Map;

public class EventMassiveCorePlayercleanToleranceMillis extends EventMassiveCore
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
	
	private final long now;
	public long getNow() { return now; }
	
	private final SenderEntity<?> entity;
	public SenderEntity<?> getEntity() { return this.entity; }
	
	public SenderColl<?> getColl() { return entity.getColl(); }
	
	private final Map<String, Long> toleranceCauseMillis = new LinkedHashMap<>();
	public Map<String, Long> getToleranceCauseMillis() { return this.toleranceCauseMillis; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public EventMassiveCorePlayercleanToleranceMillis(SenderEntity entity)
	{
		this(System.currentTimeMillis(), entity);
	}
	
	public EventMassiveCorePlayercleanToleranceMillis(long now, SenderEntity entity)
	{
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
	
	public boolean shouldBeRemoved()
	{
		long toleranceMillis = getToleranceMillis();
		
		long now = this.getNow();
		long lastActivityMillis = ((Inactive)this.getEntity()).getLastActivityMillis();
		
		long removeTime = lastActivityMillis + toleranceMillis;
		
		return now >= removeTime;
	}
	
}
