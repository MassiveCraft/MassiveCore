package com.massivecraft.mcore;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.massivecraft.mcore.event.MCoreAfterPlayerRespawnEvent;
import com.massivecraft.mcore.event.MCoreAfterPlayerTeleportEvent;
import com.massivecraft.mcore.event.MCorePermissionDeniedFormatEvent;
import com.massivecraft.mcore.event.MCorePlayerLeaveEvent;
import com.massivecraft.mcore.event.MCorePlayerToRecipientChatEvent;
import com.massivecraft.mcore.event.MCoreSenderRegisterEvent;
import com.massivecraft.mcore.event.MCoreSenderUnregisterEvent;
import com.massivecraft.mcore.mixin.Mixin;
import com.massivecraft.mcore.store.Coll;
import com.massivecraft.mcore.store.SenderColl;
import com.massivecraft.mcore.util.SenderUtil;
import com.massivecraft.mcore.util.SmokeUtil;

public class InternalListener implements Listener
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static InternalListener i = new InternalListener();
	public static InternalListener get() { return i; }
	
	// -------------------------------------------- //
	// REGISTER
	// -------------------------------------------- //
	
	public void setup()
	{
		MCorePlayerLeaveEvent.player2event.clear();
		Bukkit.getPluginManager().registerEvents(this, MCore.get());
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
		if (!MCoreConf.get().isUsingRecipientChatEvent()) return;
		
		// Prepare vars
		MCorePlayerToRecipientChatEvent recipientEvent;
		final Player sender = event.getPlayer();
		String message = event.getMessage();
		String format = event.getFormat();
		
		// Pick the recipients to avoid the message getting sent without canceling the event.
		Set<Player> players = new HashSet<Player>(event.getRecipients());
		event.getRecipients().clear();
		
		// For each of the players
		for (Player recipient : players)
		{
			// Run the event for this unique recipient
			recipientEvent = new MCorePlayerToRecipientChatEvent(event.isAsynchronous(), sender, recipient, message, format);
			recipientEvent.run();
			
			// Format and send with the format and message from this recipient's own event. 
			String recipientMessage = String.format(recipientEvent.getFormat(), sender.getDisplayName(), recipientEvent.getMessage());
			recipient.sendMessage(recipientMessage);
		}
		
		// For the console
		recipientEvent = new MCorePlayerToRecipientChatEvent(event.isAsynchronous(), sender, Bukkit.getConsoleSender(), message, format);
		recipientEvent.run();
		event.setMessage(recipientEvent.getMessage());
		event.setFormat(recipientEvent.getFormat());
	}
	
	// -------------------------------------------- //
	// PERMISSION DENIED FORMAT
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void permissionDeniedFormat(MCorePermissionDeniedFormatEvent event)
	{
		// If noone set a format already ...
		if (event.hasFormat()) return;
		
		// ... and we have a custom format in the config ...
		String customFormat = MCoreConf.get().getPermissionDeniedFormat(event.getPermissionName());
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
		
		// Get the lowercased token
		String tokenlc = event.getLastToken().toLowerCase();
		
		// Create a case insensitive set to check for already added stuff
		Set<String> current = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		current.addAll(event.getTabCompletions());
		
		// Add ids of all online senders that match and isn't added yet. 
		for (String senderId : Mixin.getOnlineSenderIds())
		{
			if (!senderId.toLowerCase().startsWith(tokenlc)) continue;
			if (current.contains(senderId)) continue;
			if (!Mixin.canSee(watcher, senderId)) continue;
			
			event.getTabCompletions().add(senderId);
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
	// PLAYER AND SENDER REFERENCES
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void senderReferencesLoginLowest(PlayerLoginEvent event)
	{
		String id = SenderUtil.getSenderId(event.getPlayer());
		CommandSender sender = event.getPlayer();
		
		SenderColl.setSenderRefferences(id, sender);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void senderReferencesLoginMonitor(PlayerLoginEvent event)
	{
		if (event.getResult() == Result.ALLOWED) return;
		
		String id = SenderUtil.getSenderId(event.getPlayer());
		CommandSender sender = null;
		
		SenderColl.setSenderRefferences(id, sender);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void senderReferencesQuitMonitor(PlayerQuitEvent event)
	{
		String id = SenderUtil.getSenderId(event.getPlayer());
		CommandSender sender = null;
		
		SenderColl.setSenderRefferences(id, sender);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void senderReferencesRegisterMonitor(MCoreSenderRegisterEvent event)
	{
		String id = SenderUtil.getSenderId(event.getSender());
		CommandSender sender = event.getSender();
		
		SenderColl.setSenderRefferences(id, sender);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void senderReferencesUnregisterMonitor(MCoreSenderUnregisterEvent event)
	{
		String id = SenderUtil.getSenderId(event.getSender());
		CommandSender sender = null;
		
		SenderColl.setSenderRefferences(id, sender);
	}
	
	// -------------------------------------------- //
	// AFTER EVENTS
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void after(PlayerTeleportEvent event)
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(MCore.get(), new MCoreAfterPlayerTeleportEvent(event), 0);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void after(PlayerRespawnEvent event)
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(MCore.get(), new MCoreAfterPlayerRespawnEvent(event, event.getPlayer().getLocation()), 0);
	}
	
	// -------------------------------------------- //
	// EVENT TOOL: causedByKick
	// -------------------------------------------- //
	
	public static Map<String,String> kickedPlayerReasons = new HashMap<String,String>();
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void causedByKick(PlayerKickEvent event)
	{
		final String name = event.getPlayer().getName();
		kickedPlayerReasons.put(name, event.getReason());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void causedByKick(PlayerQuitEvent event)
	{
		// We do the schedule in order for the set to be correct through out the whole MONITOR priority state.
		final String name = event.getPlayer().getName();
		Bukkit.getScheduler().scheduleSyncDelayedTask(MCore.get(), new Runnable()
		{
			@Override
			public void run()
			{
				kickedPlayerReasons.remove(name);
			}
		});
	}
	
	// -------------------------------------------- //
	// PLAYER LEAVE EVENT
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void leaveEventKickCall(PlayerKickEvent event)
	{
		new MCorePlayerLeaveEvent(event.getPlayer(), true, "kick", event.getReason()).run();
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void leaveEventQuitCall(PlayerQuitEvent event)
	{
		new MCorePlayerLeaveEvent(event.getPlayer(), false, "quit", null).run();
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void leaveEventQuitClear(PlayerQuitEvent event)
	{
		// We do the schedule in order for the set to be correct through out the whole MONITOR priority state.
		final String name = event.getPlayer().getName();
		Bukkit.getScheduler().scheduleSyncDelayedTask(MCore.get(), new Runnable()
		{
			@Override
			public void run()
			{
				MCorePlayerLeaveEvent.player2event.remove(name);
			}
		});
	}
	
	// -------------------------------------------- //
	// SYNC PLAYER ON LOGON AND LEAVE
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void syncOnPlayerLogin(PlayerLoginEvent event)
	{
		//MCore.get().log("LOWEST syncOnPlayerLogin", event.getPlayer().getName());
		this.syncAllForPlayer(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void syncOnPlayerLeave(MCorePlayerLeaveEvent event)
	{
		//MCore.get().log("MONITOR syncOnPlayerLeave", event.getPlayer().getName());
		this.syncAllForPlayer(event.getPlayer());
	}
	
	public void syncAllForPlayer(Player player)
	{
		String playerName = player.getName();
		for (Coll<?> coll : Coll.getInstances())
		{
			if (!(coll instanceof SenderColl)) continue;
			SenderColl<?> pcoll = (SenderColl<?>)coll;
			pcoll.syncId(playerName);
		}
	}
}
