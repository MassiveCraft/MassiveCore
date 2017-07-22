package com.massivecraft.massivecore.engine;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveCoreMConf;
import com.massivecraft.massivecore.event.EventMassiveCorePlayerCleanInactivityToleranceMillis;
import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.cleanable.CleaningUtil;
import com.massivecraft.massivecore.util.IdUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.Collections;
import java.util.List;

public class EngineMassiveCoreClean extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineMassiveCoreClean i = new EngineMassiveCoreClean();
	public static EngineMassiveCoreClean get() { return i; }
	public EngineMassiveCoreClean()
	{
		// Just check once a minute
		this.setPeriod(60L * 20L);
	}
	
	// -------------------------------------------- //
	// OVERRIDE: RUNNABLE
	// -------------------------------------------- //
	
	@Override
	public void run()
	{
		final long now = System.currentTimeMillis();
		
		// If this is the right server ...
		if ( ! MassiveCore.isTaskServer()) return;
		
		// ... and the current invocation ...
		final long currentInvocation = getInvocationFromMillis(now);
		
		// ... and the last invocation ...
		final long lastInvocation = getInvocationFromMillis(MassiveCoreMConf.get().cleanTaskLastMillis);
		
		// ... are different ...
		if (currentInvocation == lastInvocation) return;
		
		// ... then it's time to invoke.
		invoke(now);
	}
	
	public void invoke(long now)
	{
		// Update lastMillis
		MassiveCoreMConf.get().cleanTaskLastMillis = now;
		MassiveCoreMConf.get().changed();
		
		List<CommandSender> recipients = Collections.<CommandSender>singletonList(IdUtil.getConsole());
		for (Coll<?> coll : Coll.getInstances())
		{
			if (!coll.isCleanTaskEnabled()) continue;
			CleaningUtil.considerClean(now, coll, recipients);
		}
	}
	
	// -------------------------------------------- //
	// TASK MILLIS AND INVOCATION
	// -------------------------------------------- //
	// The invocation is the amount of periods from UNIX time to now.
	// It will increment by one when a period has passed.
	
	// Remember to check isDisabled first!
	// Here we accept millis from inside the period by rounding down.
	private static long getInvocationFromMillis(long millis)
	{
		return (millis - MassiveCoreMConf.get().cleanTaskOffsetMillis) / MassiveCoreMConf.get().cleanTaskPeriodMillis;
	}

	// -------------------------------------------- //
	// DEFAULT
	// -------------------------------------------- //
	
	@EventHandler(priority =  EventPriority.LOWEST, ignoreCancelled = true)
	public void defaultMillis(EventMassiveCorePlayerCleanInactivityToleranceMillis event)
	{
		event.getToleranceCauseMillis().put("Default", event.getColl().getCleanInactivityToleranceMillis());
	}
	
}
