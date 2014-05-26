package com.massivecraft.mcore.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import com.massivecraft.mcore.EngineAbstract;
import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.event.EventMCorePlayerUpdate;
import com.massivecraft.mcore.event.MCoreAfterPlayerRespawnEvent;
import com.massivecraft.mcore.event.MCoreAfterPlayerTeleportEvent;

public class PlayerUtil extends EngineAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static PlayerUtil i = new PlayerUtil();
	public static PlayerUtil get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void activate()
	{
		super.activate();
		
		idToDeath.clear();
		
		joinedPlayerIds.clear();
		for (Player player : Bukkit.getOnlinePlayers())
		{
			joinedPlayerIds.add(player.getUniqueId());
		}
		
		idToLastMoveMillis.clear();
	}
	
	@Override
	public Plugin getPlugin()
	{
		return MCore.get();
	}
	
	// -------------------------------------------- //
	// IS JOINED
	// -------------------------------------------- //
	
	private static Set<UUID> joinedPlayerIds = new ConcurrentSkipListSet<UUID>();
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void isJoined(PlayerJoinEvent event)
	{
		final UUID id = event.getPlayer().getUniqueId();
		Bukkit.getScheduler().scheduleSyncDelayedTask(MCore.get(), new Runnable()
		{
			@Override
			public void run()
			{
				joinedPlayerIds.add(id);
			}
		});
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void isJoined(PlayerQuitEvent event)
	{
		final UUID id = event.getPlayer().getUniqueId();
		joinedPlayerIds.remove(id);
	}
	
	public static boolean isJoined(Player player)
	{
		if (player == null) throw new NullPointerException("player was null");
		final UUID id = player.getUniqueId();
		return joinedPlayerIds.contains(id);
		
	}
	
	// -------------------------------------------- //
	// LAST MOVE & STAND STILL (MILLIS)
	// -------------------------------------------- //
	
	private static Map<UUID, Long> idToLastMoveMillis = new HashMap<UUID, Long>(); 
	
	public static void setLastMoveMillis(Player player, long millis)
	{
		if (player == null) return;
		idToLastMoveMillis.put(player.getUniqueId(), millis);
	}
	
	public static void setLastMoveMillis(Player player)
	{
		setLastMoveMillis(player, System.currentTimeMillis());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void setLastMoveMillis(PlayerMoveEvent event)
	{
		if (MUtil.isSameBlock(event)) return;
		setLastMoveMillis(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void setLastMoveMillis(PlayerJoinEvent event)
	{
		setLastMoveMillis(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void setLastMoveMillis(PlayerChangedWorldEvent event)
	{
		setLastMoveMillis(event.getPlayer());
	}
	
	public static long getLastMoveMillis(Player player)
	{
		if (player == null) return 0;
		Long ret = idToLastMoveMillis.get(player.getUniqueId());
		if (ret == null) return 0;
		return ret;
	}
	
	public static long getStandStillMillis(Player player)
	{
		if (player == null) return 0;
		if (player.isDead()) return 0;
		if (!player.isOnline()) return 0;
		
		Long ret = idToLastMoveMillis.get(player.getUniqueId());
		if (ret == null) return 0;
		
		ret = System.currentTimeMillis() - ret;
		
		return ret;
	}
	
	// -------------------------------------------- //
	// IS DUPLICATE DEATH EVENT
	// -------------------------------------------- //
	// Some times when players die the PlayerDeathEvent is fired twice.
	// We want to ignore the extra calls.
	
	private static Map<UUID, PlayerDeathEvent> idToDeath = new HashMap<UUID, PlayerDeathEvent>();
	
	public static boolean isDuplicateDeathEvent(PlayerDeathEvent event)
	{
		// Prepare the lowercase name ...
		final UUID id = event.getEntity().getUniqueId();
		
		// ... take a look at the currently stored event ...
		PlayerDeathEvent current = idToDeath.get(id);
		
		if (current != null) return !current.equals(event);
		
		// ... otherwise store ... 
		idToDeath.put(id, event);
		
		// ... schedule removal ...
		Bukkit.getScheduler().scheduleSyncDelayedTask(MCore.get(), new Runnable()
		{
			@Override
			public void run()
			{
				idToDeath.remove(id);
			}
		});
		
		// ... and return the fact that it was not a duplicate.
		return false;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void isDuplicateDeathEventLowest(PlayerDeathEvent event)
	{
		isDuplicateDeathEvent(event);
	}
	
	// -------------------------------------------- //
	// PACKET
	// -------------------------------------------- //
	
	/**
	 * Updates the players food and health information.
	 */
	public static void sendHealthFoodUpdatePacket(Player player)
	{
		// No need for nms anymore.
		// We can trigger this packet through the use of this bukkit api method:
		player.setHealthScaled(player.isHealthScaled());
		/*
		CraftPlayer cplayer = (CraftPlayer)player;
		EntityPlayer eplayer = cplayer.getHandle();
		eplayer.playerConnection.sendPacket(new PacketPlayOutUpdateHealth(cplayer.getScaledHealth(), eplayer.getFoodData().a(), eplayer.getFoodData().e()));
		*/
	}
	
	// -------------------------------------------- //
	// SETTINGS BY EVENT
	// -------------------------------------------- //
	
	public static void update(Player player)
	{
		EventMCorePlayerUpdate event = new EventMCorePlayerUpdate(player);
		event.run();
		
		setMaxHealth(player, event.getMaxHealth());
		setFlyAllowed(player, event.isFlyAllowed());
		setFlyActive(player, event.isFlyActive());
		setFlySpeed(player, event.getFlySpeed());
	}
	
	public static void reset(Player player)
	{
		setMaxHealth(player, getMaxHealthDefault(player));
		setFlyAllowed(player, isFlyAllowedDefault(player));
		setFlyActive(player, isFlyActiveDefault(player));
		setFlySpeed(player, getFlySpeedDefault(player));
		
		update(player);
	}
	
	// Can't be cancelled
	@EventHandler(priority = EventPriority.LOWEST)
	public void reset(PlayerJoinEvent event)
	{
		// If we have a player ...
		Player player = event.getPlayer();
		
		// ... and the player is alive ...
		if (player.isDead()) return;
		
		// ... trigger.
		reset(player);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void update(MCoreAfterPlayerTeleportEvent event)
	{
		// If we have a player ...
		Player player = event.getPlayer();
		
		// ... and the player is alive ...
		if (player.isDead()) return;
		
		// ... trigger.
		update(player);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void update(MCoreAfterPlayerRespawnEvent event)
	{
		// If we have a player ...
		Player player = event.getPlayer();
		
		// ... and the player is alive ...
		if (player.isDead()) return;
		
		// ... trigger.
		update(player);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void update(PlayerChangedWorldEvent event)
	{
		// If we have a player ...
		Player player = event.getPlayer();
		
		// ... and the player is alive ...
		if (player.isDead()) return;
		
		// ... trigger.
		update(player);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void update(PlayerMoveEvent event)
	{
		// If we have a player ...
		Player player = event.getPlayer();
		
		// ... and the player is alive ...
		if (player.isDead()) return;
		
		// ... and the player moved from one block to another ...
		if (event.getFrom().getBlock().equals(event.getTo().getBlock())) return;
		
		// ... trigger.
		update(player);
	}

	// -------------------------------------------- //
	// MAX HEALTH
	// -------------------------------------------- //

	public static boolean setMaxHealth(Player player, double maxHealth)
	{	
		// NoChange
		if (getMaxHealth(player) == maxHealth) return false;
		
		// Apply
		player.setMaxHealth(maxHealth);
		
		// Return
		return true;
	}
	
	public static double getMaxHealth(Player player)
	{
		return player.getMaxHealth();
	}
	
	public static double getMaxHealthDefault(Player player)
	{
		return 20D;
	}
	
	// -------------------------------------------- //
	// FLY: ALLOWED
	// -------------------------------------------- //
	
	public static boolean setFlyAllowed(Player player, boolean allowed)
	{	
		// NoChange
		if (isFlyAllowed(player) == allowed) return false;
		
		// Apply
		player.setFallDistance(0);
		player.setAllowFlight(allowed);
		player.setFallDistance(0);
		
		// Return
		return true;
	}
	
	public static boolean isFlyAllowed(Player player)
	{
		return player.getAllowFlight();
	}
	
	public static boolean isFlyAllowedDefault(Player player)
	{
		return player.getGameMode() == GameMode.CREATIVE;
	}
	
	// -------------------------------------------- //
	// FLY: ACTIVE
	// -------------------------------------------- //
	
	public static Map<UUID, Long> idToLastFlyActive = new HashMap<UUID, Long>();
	public static Long getLastFlyActive(Player player)
	{
		return idToLastFlyActive.get(player.getUniqueId());
	}
	public static void setLastFlyActive(Player player, Long millis)
	{
		idToLastFlyActive.put(player.getUniqueId(), millis);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void negateNoCheatPlusBug(EntityDamageEvent event)
	{
		// If a player ...
		if ( ! (event.getEntity() instanceof Player)) return;
		Player player = (Player)event.getEntity();
		
		// ... is taking fall damage ...
		if (event.getCause() != DamageCause.FALL) return;
		
		// ... within 2 seconds of flying ...
		Long lastActive = getLastFlyActive(player);
		if (lastActive == null) return;
		if (System.currentTimeMillis() - lastActive > 2000) return;
		
		// ... cancel the event.
		event.setCancelled(true);
	}
	
	public static boolean setFlyActive(Player player, boolean active)
	{
		// Last Active Update
		if (active)
		{
			setLastFlyActive(player, System.currentTimeMillis());
		}
		
		// NoChange
		if (isFlyActive(player) == active) return false;
		
		// Apply
		player.setFallDistance(0);
		player.setFlying(active);
		player.setFallDistance(0);
		
		// Return
		return true;
	}
	
	public static boolean isFlyActive(Player player)
	{
		return player.isFlying();
	}
	
	public static boolean isFlyActiveDefault(Player player)
	{
		return player.getGameMode() == GameMode.CREATIVE;
	}
	
	// -------------------------------------------- //
	// FLY: SPEED
	// -------------------------------------------- //
	
	public final static float DEFAULT_FLY_SPEED = 0.1f;
	
	public static boolean setFlySpeed(Player player, float speed)
	{
		// NoChange
		if (getFlySpeed(player) == speed) return false;
		
		// Apply
		player.setFallDistance(0);
		player.setFlySpeed(speed);
		player.setFallDistance(0);
		
		// Return
		return true;
	}
	
	public static float getFlySpeed(Player player)
	{
		return player.getFlySpeed();
	}
	
	public static float getFlySpeedDefault(Player player)
	{
		return DEFAULT_FLY_SPEED;
	} 
	
}
