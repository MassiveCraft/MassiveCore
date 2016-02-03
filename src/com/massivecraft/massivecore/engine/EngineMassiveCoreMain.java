package com.massivecraft.massivecore.engine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;
import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveCoreMConf;
import com.massivecraft.massivecore.SenderPresence;
import com.massivecraft.massivecore.SenderType;
import com.massivecraft.massivecore.event.EventMassiveCoreAfterPlayerRespawn;
import com.massivecraft.massivecore.event.EventMassiveCoreAfterPlayerTeleport;
import com.massivecraft.massivecore.event.EventMassiveCorePermissionDeniedFormat;
import com.massivecraft.massivecore.event.EventMassiveCorePlayerToRecipientChat;
import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.predicate.Predicate;
import com.massivecraft.massivecore.predicate.PredicateStartsWithIgnoreCase;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.SmokeUtil;

public class EngineMassiveCoreMain extends EngineAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineMassiveCoreMain i = new EngineMassiveCoreMain();
	public static EngineMassiveCoreMain get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Plugin getPlugin()
	{
		return MassiveCore.get();
	}
	
	@Override
	public void activate()
	{
		super.activate();
	}
	
	// -------------------------------------------- //
	// RECIPIENT CHAT
	// -------------------------------------------- //
	// A system to create per recipient events.
	// It clears the recipient set so the event isn't cancelled completely.
	// It will cause non async chat events not to fire.
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void recipientChat(final AsyncPlayerChatEvent event)
	{
		// Return unless we are using the recipient chat event
		if (!MassiveCoreMConf.get().usingRecipientChatEvent) return;
		
		// Prepare vars
		EventMassiveCorePlayerToRecipientChat recipientEvent;
		final Player sender = event.getPlayer();
		if (MUtil.isntPlayer(sender)) return;
		
		String message = event.getMessage();
		String format = event.getFormat();
		
		// Pick the recipients to avoid the message getting sent without canceling the event.
		Set<Player> players = new HashSet<Player>(event.getRecipients());
		event.getRecipients().clear();
		
		// For each of the players
		for (Player recipient : players)
		{
			// Run the event for this unique recipient
			recipientEvent = new EventMassiveCorePlayerToRecipientChat(event.isAsynchronous(), sender, recipient, message, format);
			recipientEvent.run();
			
			// Format and send with the format and message from this recipient's own event. 
			String recipientMessage = String.format(recipientEvent.getFormat(), sender.getDisplayName(), recipientEvent.getMessage());
			recipient.sendMessage(recipientMessage);
		}
		
		// For the console
		recipientEvent = new EventMassiveCorePlayerToRecipientChat(event.isAsynchronous(), sender, Bukkit.getConsoleSender(), message, format);
		recipientEvent.run();
		event.setMessage(recipientEvent.getMessage());
		event.setFormat(recipientEvent.getFormat());
	}
	
	// -------------------------------------------- //
	// PERMISSION DENIED FORMAT
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void permissionDeniedFormat(EventMassiveCorePermissionDeniedFormat event)
	{
		// If noone set a format already ...
		if (event.hasFormat()) return;
		
		// ... and we have a custom format in the config ...
		String customFormat = MassiveCoreMConf.get().getPermissionDeniedFormat(event.getPermissionName());
		if (customFormat == null) return;
		
		// ... then make use of that format.
		event.setFormat(customFormat);
	}
	
	// -------------------------------------------- //
	// CHAT TAB COMPLETE
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void chatTabComplete(PlayerChatTabCompleteEvent event)
	{
		// So the player is watching ...
		Player watcher = event.getPlayer();
		if (MUtil.isntPlayer(watcher)) return;
		
		// Get the lowercased token predicate
		Predicate<String> predicate = PredicateStartsWithIgnoreCase.get(event.getLastToken());
		
		// Create a case insensitive set to check for already added stuff
		Set<String> current = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		current.addAll(event.getTabCompletions());
		
		// Add names of all online senders that match and isn't added yet.
		// TODO: Should this only be players? Would a player actually want to tab-complete @console?
		for (String senderName : IdUtil.getNames(SenderPresence.ONLINE, SenderType.ANY))
		{
			if ( ! predicate.apply(senderName)) continue;
			if (current.contains(senderName)) continue;
			if ( ! Mixin.isVisible(senderName, watcher)) continue;
			
			event.getTabCompletions().add(senderName);
		}
	}
	
	// -------------------------------------------- //
	// EXPLOSION FX
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void explosionFx(EntityDamageByBlockEvent event)
	{
		// If an entity is taking damage from a block explosion ...
		DamageCause cause = event.getCause();
		if (cause != DamageCause.BLOCK_EXPLOSION) return;
		
		// ... and that explosion is a fake ...
		if (!SmokeUtil.fakeExplosion.booleanValue()) return;
		
		// ... then cancel the event and the damage.
		event.setCancelled(true);
	}
	
	// -------------------------------------------- //
	// AFTER EVENTS
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void after(PlayerTeleportEvent event)
	{
		Player player = event.getPlayer();
		if (MUtil.isntPlayer(player)) return;
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(MassiveCore.get(), new EventMassiveCoreAfterPlayerTeleport(event), 0);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void after(PlayerRespawnEvent event)
	{
		Player player = event.getPlayer();
		if (MUtil.isntPlayer(player)) return;
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(MassiveCore.get(), new EventMassiveCoreAfterPlayerRespawn(event, player.getLocation()), 0);
	}
	
	// -------------------------------------------- //
	// EVENT TOOL: causedByKick
	// -------------------------------------------- //
	
	public static Map<UUID, String> kickedPlayerReasons = new HashMap<UUID, String>();
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void causedByKick(PlayerKickEvent event)
	{
		Player player = event.getPlayer();
		if (MUtil.isntPlayer(player)) return;
		final UUID uuid = player.getUniqueId();
		
		kickedPlayerReasons.put(uuid, event.getReason());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void causedByKick(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		if (MUtil.isntPlayer(player)) return;
		final UUID uuid = player.getUniqueId();
		
		// We do the schedule in order for the set to be correct through out the whole MONITOR priority state.
		Bukkit.getScheduler().scheduleSyncDelayedTask(MassiveCore.get(), new Runnable()
		{
			@Override
			public void run()
			{
				kickedPlayerReasons.remove(uuid);
			}
		});
	}
	
}
