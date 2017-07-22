package com.massivecraft.massivecore.store.cleanable;

import com.massivecraft.massivecore.mixin.MixinMessage;
import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.TimeUnit;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.command.CommandSender;

import java.util.Collection;

public class CleaningUtil
{
	// -------------------------------------------- //
	// WHAT DO WE HANDLE?
	// -------------------------------------------- //
	
	// The standard used for non-crucial data that can easily be replaced.
	public static final long CLEAN_INACTIVITY_TOLERANCE_MILLIS_STANDARD = 3 * TimeUnit.MILLIS_PER_MONTH;
	
	// The standard for important information that can not easily be replaced.
	public static final long CLEAN_INACTIVITY_TOLERANCE_MILLIS_IMPORTANT = 15 * TimeUnit.MILLIS_PER_MONTH;
	
	// -------------------------------------------- //
	// LOGIC
	// -------------------------------------------- //
	
	public static void considerClean(final Coll<? extends Cleanable> coll, final Iterable<CommandSender> recipients)
	{
		considerClean(System.currentTimeMillis(), coll, recipients);
	}
	
	public static void considerClean(long now, final Coll<?> coll, final Iterable<CommandSender> recipients)
	{
		final long start = System.nanoTime();
		int count = 0;
		
		final Collection<? extends Entity<?>> entities = coll.getAll();
		
		// For each entity ...
		for (Entity<?> entity : entities)
		{
			// ... see if they should be cleaned.
			boolean result = considerClean(now, entity);
			if (result) count++;
		}
		
		long nano = System.nanoTime() - start;
		int current = coll.getIds().size();
		int total = current + count;
		double percentage = (((double) count) / total) * 100D;
		if (!MUtil.isFinite(percentage)) percentage = 0D;
		String message = Txt.parse("<i>Removed <h>%d<i>/<h>%d (%.2f%%) <i>entities from <h>%s <i>took <v>%.2fms<i>.", count, total, percentage, coll.getName(), nano/1000D);
		for (CommandSender recipient : recipients)
		{
			MixinMessage.get().messageOne(recipient, message);
		}
	}
	
	public static boolean considerClean(long now, Entity<?> entity)
	{
		if (entity.detached()) return false;
		
		// Consider
		if (!shouldBeCleaned(now, entity)) return false;
		
		// Apply
		clean(entity);
		
		return true;
	}
	
	public static void clean(Entity<?> entity)
	{
		((Cleanable) entity).preClean();
		entity.detach();
		((Cleanable) entity).postClean();
	}
	
	public static boolean shouldBeCleaned(long now, Entity<?> entity)
	{
		if (!(entity instanceof Cleanable)) return false;
		return ((Cleanable) entity).shouldBeCleaned(now);
	}
	
}
