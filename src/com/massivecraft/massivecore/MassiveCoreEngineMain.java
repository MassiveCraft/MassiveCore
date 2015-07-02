package com.massivecraft.massivecore;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.event.EventMassiveCoreAfterPlayerRespawn;
import com.massivecraft.massivecore.event.EventMassiveCoreAfterPlayerTeleport;
import com.massivecraft.massivecore.event.EventMassiveCorePermissionDeniedFormat;
import com.massivecraft.massivecore.event.EventMassiveCorePlayerLeave;
import com.massivecraft.massivecore.event.EventMassiveCorePlayerToRecipientChat;
import com.massivecraft.massivecore.event.EventMassiveCoreSenderRegister;
import com.massivecraft.massivecore.event.EventMassiveCoreSenderUnregister;
import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.SenderColl;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.SmokeUtil;
import com.massivecraft.massivecore.xlib.gson.JsonElement;

public class MassiveCoreEngineMain extends EngineAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MassiveCoreEngineMain i = new MassiveCoreEngineMain();
	public static MassiveCoreEngineMain get() { return i; }
	
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
		EventMassiveCorePlayerLeave.player2event.clear();
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
		Predictate<String> predictate = PredictateStartsWithIgnoreCase.get(event.getLastToken());
		
		// Create a case insensitive set to check for already added stuff
		Set<String> current = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		current.addAll(event.getTabCompletions());
		
		// Add names of all online senders that match and isn't added yet.
		// TODO: Should this only be players? Would a player actually want to tab-complete @console?
		for (String senderName : IdUtil.getNames(SenderPresence.ONLINE, SenderType.ANY))
		{
			if (!predictate.apply(senderName)) continue;
			if (current.contains(senderName)) continue;
			if (!Mixin.canSee(watcher, senderName)) continue;
			
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
	// PLAYER AND SENDER REFERENCES
	// -------------------------------------------- //
	// Note: For now we update both names and ids.
	// That way collections in plugins that haven't yet undergone update will still work.
	
	// This method sets the sender reference to what you decide.
	public static void setSenderReferences(CommandSender sender, CommandSender reference)
	{
		if (MUtil.isntSender(sender)) return;
		
		String id = IdUtil.getId(sender);
		if (id != null)
		{
			SenderColl.setSenderReferences(id, reference);
		}
		
		String name = IdUtil.getName(sender);
		if (name != null)
		{
			SenderColl.setSenderReferences(name, reference);
		}
	}
	
	// This method sets the sender reference based on it's online state.
	public static void setSenderReferences(Player player)
	{
		Player reference = player;
		if ( ! player.isOnline()) reference = null;
		setSenderReferences(player, reference);
	}
	
	// Same as above but next tick.
	public static void setSenderReferencesSoon(final Player player)
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(MassiveCore.get(), new Runnable()
		{
			@Override
			public void run()
			{
				setSenderReferences(player);
			}
		});
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void setSenderReferencesLoginLowest(PlayerLoginEvent event)
	{
		final Player player = event.getPlayer();
		
		// We set the reference at LOWEST so that it's present during this PlayerLoginEvent event.
		setSenderReferences(player, player);
		
		// And the next tick we update the reference based on it's online state.
		// Not all players succeed in logging in. They may for example be banned.
		setSenderReferencesSoon(player);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void setSenderReferencesQuitMonitor(PlayerQuitEvent event)
	{
		// PlayerQuitEvents are /probably/ trustworthy.
		// We check ourselves the next tick just to be on the safe side.
		setSenderReferencesSoon(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void setSenderReferencesRegisterMonitor(EventMassiveCoreSenderRegister event)
	{
		// This one we can however trust.
		setSenderReferences(event.getSender(), event.getSender());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void setSenderReferencesUnregisterMonitor(EventMassiveCoreSenderUnregister event)
	{
		// This one we can however trust.
		setSenderReferences(event.getSender(), null);
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
	
	// -------------------------------------------- //
	// PLAYER LEAVE EVENT
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void leaveEventKickCall(PlayerKickEvent event)
	{
		Player player = event.getPlayer();
		if (MUtil.isntPlayer(player)) return;
		
		new EventMassiveCorePlayerLeave(player, true, "kick", event.getReason()).run();
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void leaveEventQuitCall(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		if (MUtil.isntPlayer(player)) return;
		
		new EventMassiveCorePlayerLeave(player, false, "quit", null).run();
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void leaveEventQuitClear(PlayerQuitEvent event)
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
				EventMassiveCorePlayerLeave.player2event.remove(uuid);
			}
		});
	}
	
	// -------------------------------------------- //
	// MASSIVE STORE: LOGIN SYNC
	// -------------------------------------------- //
	// This section handles the automatic sync of a players corresponding massive store entries on login.
	// If possible the database IO is made during the AsyncPlayerPreLoginEvent to offloat the main server thread.
	
	protected Map<String, Map<SenderColl<?>, Entry<JsonElement, Long>>> idToRemoteEntries = new ConcurrentHashMap<String, Map<SenderColl<?>, Entry<JsonElement, Long>>>();
	
	// Intended to be ran asynchronously.
	public void storeRemoteEntries(final String playerId)
	{
		// Create remote entries ...
		Map<SenderColl<?>, Entry<JsonElement, Long>> remoteEntries = createRemoteEntries(playerId);
		
		// ... store them ...
		this.idToRemoteEntries.put(playerId, remoteEntries);
		
		// ... and make sure they are removed after 30 seconds.
		// Without this we might cause a memory leak.
		// Players might trigger AsyncPlayerPreLoginEvent but not PlayerLoginEvent.
		// Using WeakHashMap is not an option since the player object does not exist at AsyncPlayerPreLoginEvent.
		Bukkit.getScheduler().runTaskLaterAsynchronously(this.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				idToRemoteEntries.remove(playerId);
			}
		}, 20*30);
	}
	
	// Intended to be ran synchronously.
	// It will use remoteEntries from AsyncPlayerPreLoginEvent if possible.
	// If no such remoteEntries are available it will create them and thus lock the main server thread a bit.
	public Map<SenderColl<?>, Entry<JsonElement, Long>> getRemoteEntries(String playerId)
	{
		// If there are stored remote entries we used those ...
		Map<SenderColl<?>, Entry<JsonElement, Long>> ret = idToRemoteEntries.remove(playerId);	
		if (ret != null) return ret;
		
		// ... otherwise we create brand new ones.
		return createRemoteEntries(playerId);
	}
	
	// Used by the two methods above.
	public Map<SenderColl<?>, Entry<JsonElement, Long>> createRemoteEntries(String playerId)
	{
		// Create Ret
		Map<SenderColl<?>, Entry<JsonElement, Long>> ret = new HashMap<SenderColl<?>, Entry<JsonElement, Long>>();
		
		// Fill Ret
		for (final SenderColl<?> coll : Coll.getSenderInstances())
		{
			Entry<JsonElement, Long> remoteEntry = coll.getDb().load(coll, playerId);
			ret.put(coll, remoteEntry);
		}
		
		// Return Ret
		return ret;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void massiveStoreLoginSync(AsyncPlayerPreLoginEvent event)
	{
		// DEBUG
		// long before = System.nanoTime();
		
		// If the login was allowed ...
		if (event.getLoginResult() != Result.ALLOWED) return;
		
		// ... get player id ...
		final String playerId = event.getUniqueId().toString();
		
		// ... and store the remote entries.
		this.storeRemoteEntries(playerId);
		
		// DEBUG
		// long after = System.nanoTime();
		// long duration = after - before;
		// double ms = (double)duration / 1000000D;
		// String message = Txt.parse("<i>AsyncPlayerPreLoginEvent for %s <i>took <h>%.2f <i>ms.", event.getName(), ms);
		// MassiveCore.get().log(message);
		// NOTE: I get values between 5 and 55 ms!
	}
	
	// Can not be cancelled.
	@EventHandler(priority = EventPriority.LOWEST)
	public void massiveStoreLoginSync(PlayerLoginEvent event)
	{
		// Get player id ...
		Player player = event.getPlayer();
		if (MUtil.isntPlayer(player)) return;
		final String playerId = player.getUniqueId().toString();
		
		// ... get remote entries ...
		Map<SenderColl<?>, Entry<JsonElement, Long>> remoteEntries = getRemoteEntries(playerId);
		
		// ... and sync each of them.
		for (Entry<SenderColl<?>, Entry<JsonElement, Long>> entry : remoteEntries.entrySet())
		{
			SenderColl<?> coll = entry.getKey();
			Entry<JsonElement, Long> remoteEntry = entry.getValue();
			coll.syncId(playerId, null, remoteEntry);
		}
	}
	
	// -------------------------------------------- //
	// SYNC PLAYER ON LOGON AND LEAVE
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void syncOnPlayerLeave(EventMassiveCorePlayerLeave event)
	{
		Player player = event.getPlayer();
		this.syncAllForPlayer(player);
	}
	
	public void syncAllForPlayer(Player player)
	{
		if (MUtil.isntPlayer(player)) return;
		
		String playerId = player.getUniqueId().toString();
		for (SenderColl<?> coll : Coll.getSenderInstances())
		{
			coll.syncId(playerId);
		}
	}
	
}
