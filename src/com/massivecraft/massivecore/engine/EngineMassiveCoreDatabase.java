package com.massivecraft.massivecore.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;
import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.event.EventMassiveCorePlayerLeave;
import com.massivecraft.massivecore.event.EventMassiveCoreSenderRegister;
import com.massivecraft.massivecore.event.EventMassiveCoreSenderUnregister;
import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.SenderColl;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.xlib.gson.JsonObject;

public class EngineMassiveCoreDatabase extends EngineAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineMassiveCoreDatabase i = new EngineMassiveCoreDatabase();
	public static EngineMassiveCoreDatabase get() { return i; }
	
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
	// PLAYER AND SENDER REFERENCES
	// -------------------------------------------- //
	
	public static Map<String, PlayerLoginEvent> idToPlayerLoginEvent = new MassiveMap<String, PlayerLoginEvent>();
	
	// This method sets the sender reference to what you decide.
	public static void setSenderReferences(CommandSender sender, CommandSender reference, PlayerLoginEvent event)
	{
		if (MUtil.isntSender(sender)) return;
		
		String id = IdUtil.getId(sender);
		if (id != null)
		{
			SenderColl.setSenderReferences(id, reference);
			if (event == null)
			{
				idToPlayerLoginEvent.remove(id);
			}
			else
			{
				idToPlayerLoginEvent.put(id, event);
			}
		}
	}
	
	// This method sets the sender reference based on it's online state.
	public static void setSenderReferences(Player player, PlayerLoginEvent event)
	{
		Player reference = player;
		if ( ! player.isOnline()) reference = null;
		setSenderReferences(player, reference, event);
	}
	
	// Same as above but next tick.
	public static void setSenderReferencesSoon(final Player player, final PlayerLoginEvent event)
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(MassiveCore.get(), new Runnable()
		{
			@Override
			public void run()
			{
				setSenderReferences(player, event);
			}
		});
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void setSenderReferencesLoginLowest(PlayerLoginEvent event)
	{
		final Player player = event.getPlayer();
		
		// We set the reference at LOWEST so that it's present during this PlayerLoginEvent event.
		setSenderReferences(player, player, event);
		
		// And the next tick we update the reference based on it's online state.
		// Not all players succeed in logging in. They may for example be banned.
		setSenderReferencesSoon(player, null);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void setSenderReferencesQuitMonitor(PlayerQuitEvent event)
	{
		// PlayerQuitEvents are /probably/ trustworthy.
		// We check ourselves the next tick just to be on the safe side.
		setSenderReferencesSoon(event.getPlayer(), null);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void setSenderReferencesRegisterMonitor(EventMassiveCoreSenderRegister event)
	{
		// This one we can however trust.
		setSenderReferences(event.getSender(), event.getSender(), null);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void setSenderReferencesUnregisterMonitor(EventMassiveCoreSenderUnregister event)
	{
		// This one we can however trust.
		setSenderReferences(event.getSender(), null, null);
	}
	
	// -------------------------------------------- //
	// SYNC: LOGIN
	// -------------------------------------------- //
	// This section handles the automatic sync of a players corresponding massive store entries on login.
	// If possible the database IO is made during the AsyncPlayerPreLoginEvent to offloat the main server thread.
	
	protected Map<String, Map<SenderColl<?>, Entry<JsonObject, Long>>> idToRemoteEntries = new ConcurrentHashMap<>();
	
	// Intended to be ran asynchronously.
	public void storeRemoteEntries(final String playerId)
	{
		// Create remote entries ...
		Map<SenderColl<?>, Entry<JsonObject, Long>> remoteEntries = createRemoteEntries(playerId);
		
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
	public Map<SenderColl<?>, Entry<JsonObject, Long>> getRemoteEntries(String playerId)
	{
		// If there are stored remote entries we used those ...
		Map<SenderColl<?>, Entry<JsonObject, Long>> ret = idToRemoteEntries.remove(playerId);	
		if (ret != null) return ret;
		
		// ... otherwise we create brand new ones.
		return createRemoteEntries(playerId);
	}
	
	// Used by the two methods above.
	public Map<SenderColl<?>, Entry<JsonObject, Long>> createRemoteEntries(String playerId)
	{
		// Create Ret
		Map<SenderColl<?>, Entry<JsonObject, Long>> ret = new HashMap<SenderColl<?>, Entry<JsonObject, Long>>();
		
		// Fill Ret
		for (final SenderColl<?> coll : Coll.getSenderInstances())
		{
			Entry<JsonObject, Long> remoteEntry = coll.getDb().load(coll, playerId);
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
		Map<SenderColl<?>, Entry<JsonObject, Long>> remoteEntries = getRemoteEntries(playerId);
		
		// ... and sync each of them.
		for (Entry<SenderColl<?>, Entry<JsonObject, Long>> entry : remoteEntries.entrySet())
		{
			SenderColl<?> coll = entry.getKey();
			Entry<JsonObject, Long> remoteEntry = entry.getValue();
			coll.syncId(playerId, null, remoteEntry);
		}
	}
	
	// -------------------------------------------- //
	// SYNC: LEAVE
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void syncOnPlayerLeave(EventMassiveCorePlayerLeave event)
	{
		final Player player = event.getPlayer();
		if (MUtil.isntPlayer(player)) return;
		final String id = player.getUniqueId().toString();
		for (SenderColl<?> coll : Coll.getSenderInstances())
		{
			coll.syncId(id);
		}
	}
	
}
