package com.massivecraft.massivecore.engine;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;
import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.PlayerState;
import com.massivecraft.massivecore.event.EventMassiveCorePlayerLeave;
import com.massivecraft.massivecore.util.MUtil;

public class EngineMassiveCorePlayerState extends EngineAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineMassiveCorePlayerState i = new EngineMassiveCorePlayerState();
	public static EngineMassiveCorePlayerState get() { return i; }
	
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
		
		idToState.clear();
		for (Player player : MUtil.getOnlinePlayers())
		{
			idToState.put(player.getUniqueId(), PlayerState.JOINED);
		}
	}
	
	// -------------------------------------------- //
	// STATE STORAGE
	// -------------------------------------------- //
	
	protected Map<UUID, PlayerState> idToState = new ConcurrentHashMap<UUID, PlayerState>();
	
	public PlayerState getState(UUID id)
	{
		PlayerState ret = this.idToState.get(id);
		if (ret == null) ret = PlayerState.LEFT; 
		return ret;
	}
	
	public PlayerState getState(Player player)
	{
		return this.getState(player.getUniqueId());
	}
	
	// -------------------------------------------- //
	// LOGASYNC
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void logasync(AsyncPlayerPreLoginEvent event)
	{
		final UUID id = event.getUniqueId();
		
		this.idToState.put(id, PlayerState.LOGASYNC);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void logasyncMonitor(AsyncPlayerPreLoginEvent event)
	{
		// If the player was denied entrance they are now offline.
		if (event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) return;
		
		final UUID id = event.getUniqueId();
		
		this.idToState.remove(id);
	}
	
	// -------------------------------------------- //
	// LOGSYNC
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void logsync(PlayerLoginEvent event)
	{
		final Player player = event.getPlayer();
		if (MUtil.isntPlayer(player)) return;
		final UUID id = player.getUniqueId();
		
		this.idToState.put(id, PlayerState.LOGSYNC);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void logsyncMonitor(PlayerLoginEvent event)
	{
		// If the player was denied entrance they are now offline.
		if (event.getResult() == PlayerLoginEvent.Result.ALLOWED) return;
		
		final Player player = event.getPlayer();
		if (MUtil.isntPlayer(player)) return;
		final UUID id = player.getUniqueId();
		
		this.idToState.remove(id);
	}
	
	// -------------------------------------------- //
	// JOINING
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void joining(PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();
		if (MUtil.isntPlayer(player)) return;
		final UUID id = player.getUniqueId();
		
		this.idToState.put(id, PlayerState.JOINING);
	}
	
	// -------------------------------------------- //
	// JOINED
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void joined(PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();
		if (MUtil.isntPlayer(player)) return;
		final UUID id = player.getUniqueId();
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(MassiveCore.get(), new Runnable()
		{
			@Override
			public void run()
			{
				idToState.put(id, PlayerState.JOINED);
			}
		});
	}
	
	// -------------------------------------------- //
	// LEAVING
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void leaving(EventMassiveCorePlayerLeave event)
	{
		final Player player = event.getPlayer();
		if (MUtil.isntPlayer(player)) return;
		final UUID id = player.getUniqueId();
		
		this.idToState.put(id, PlayerState.LEAVING);
	}
	
	// -------------------------------------------- //
	// LEFT
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void left(PlayerQuitEvent event)
	{
		final Player player = event.getPlayer();
		if (MUtil.isntPlayer(player)) return;
		final UUID id = player.getUniqueId();
		
		this.idToState.remove(id);
	}
	
}
