package com.massivecraft.massivecore.store.inactive;

import com.massivecraft.massivecore.event.EventMassiveCorePlayercleanToleranceMillis;
import com.massivecraft.massivecore.mixin.MixinMessage;
import com.massivecraft.massivecore.store.SenderColl;
import com.massivecraft.massivecore.store.SenderEntity;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.command.CommandSender;

import java.util.Collection;

public class InactiveUtil
{
	public static void considerRemoveInactive(final SenderColl<? extends SenderEntity<?>> coll, final Iterable<CommandSender> recipients)
	{
		considerRemoveInactive(System.currentTimeMillis(), coll, recipients);
	}
	
	public static void considerRemoveInactive(long now, final SenderColl<? extends SenderEntity<?>> coll, final Iterable<CommandSender> recipients)
	{
		final long playercleanToleranceMillis = coll.getPlayercleanToleranceMillis();
		if (playercleanToleranceMillis <= 0)
		{
			for (CommandSender recipient : recipients)
			{
				MixinMessage.get().msgOne(recipient, "<h>%s<b> does not support player cleaning.", coll.getName());
			}
			return;
		}
		
		final long start = System.currentTimeMillis();
		int count = 0;
		
		final Collection<? extends SenderEntity<?>> senderEntitiesOffline = coll.getAllOffline();
		
		// For each offline player ...
		for (SenderEntity entity : senderEntitiesOffline)
		{
			// ... see if they should be removed.
			boolean result = considerRemoveInactive(now, entity, recipients);
			if (result) count++;
		}
		
		long time = System.currentTimeMillis() - start;
		for (CommandSender recipient : recipients)
		{
			MixinMessage.get().msgOne(recipient, "<i>Removed <h>%d <i>players from <h>%s <i>took <v>%dms<i>.", count, coll.getName(), time);
		}
	}
	
	public static boolean considerRemoveInactive(long now, SenderEntity<?> entity, Iterable<CommandSender> recipients)
	{
		if ( ! (entity instanceof Inactive)) return false;
		if (entity.detached()) return false;
		
		// Consider
		if (!shouldBeRemoved(now, entity)) return false;
		
		String message = Txt.parse("<i>Player <h>%s<i> with id %s was removed due to inactivity.", entity.getName(), entity.getId());
		
		for (CommandSender recipient : recipients)
		{
			MixinMessage.get().messageOne(recipient, message);
		}
		
		// Apply
		entity.detach();
		
		return true;
	}
	
	public static boolean shouldBeRemoved(long now, SenderEntity entity)
	{
		EventMassiveCorePlayercleanToleranceMillis event = new EventMassiveCorePlayercleanToleranceMillis(now, entity);
		event.run();
		return event.shouldBeRemoved();
	}
	
}
