package com.massivecraft.massivecore.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;
import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.event.EventMassiveCorePlayerUpdate;
import com.massivecraft.massivecore.event.EventMassiveCoreAfterPlayerRespawn;
import com.massivecraft.massivecore.event.EventMassiveCoreAfterPlayerTeleport;

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
		
		idToDeathEvent.clear();
		idToDamageEvent.clear();
		idToArmSwingEvent.clear();
		
		joinedPlayerIds.clear();
		for (Player player : MUtil.getOnlinePlayers())
		{
			joinedPlayerIds.add(player.getUniqueId());
		}
		
		idToLastMoveMillis.clear();
	}
	
	@Override
	public Plugin getPlugin()
	{
		return MassiveCore.get();
	}
	
	@Override
	public Long getPeriod()
	{
		return 1L;
	}
	
	@Override
	public void run()
	{
		idToDeathEvent.clear();
		idToDamageEvent.clear();
		idToArmSwingEvent.clear();
	}
	
	// -------------------------------------------- //
	// IS JOINED
	// -------------------------------------------- //
	
	private static Set<UUID> joinedPlayerIds = new ConcurrentSkipListSet<UUID>();
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void isJoined(PlayerJoinEvent event)
	{
		final UUID id = event.getPlayer().getUniqueId();
		Bukkit.getScheduler().scheduleSyncDelayedTask(MassiveCore.get(), new Runnable()
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
	// LAST DAMAGE & NO DAMAGE (MILLIS)
	// -------------------------------------------- //
	
	private static Map<UUID, Long> idToLastDamageMillis = new HashMap<UUID, Long>(); 
	
	public static void setLastDamageMillis(Player player, long millis)
	{
		if (player == null) return;
		idToLastDamageMillis.put(player.getUniqueId(), millis);
	}
	
	public static void setLastDamageMillis(Player player)
	{
		setLastDamageMillis(player, System.currentTimeMillis());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void setLastDamageMillis(EntityDamageEvent event)
	{
		if (event.getDamage() <= 0) return;
			
		if ( ! (event.getEntity() instanceof Player)) return;
		Player player = (Player)event.getEntity();
		
		setLastDamageMillis(player);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void setLastDamageMillis(PlayerDeathEvent event)
	{
		setLastDamageMillis(event.getEntity());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void setLastDamageMillis(PlayerRespawnEvent event)
	{
		setLastDamageMillis(event.getPlayer());
	}
	
	public static long getLastDamageMillis(Player player)
	{
		if (player == null) return 0;
		Long ret = idToLastDamageMillis.get(player.getUniqueId());
		if (ret == null) return 0;
		return ret;
	}
	
	public static long getNoDamageMillis(Player player)
	{
		if (player == null) return 0;
		if (player.isDead()) return 0;
		if (!player.isOnline()) return 0;
		
		return System.currentTimeMillis() - getLastDamageMillis(player);
	}
	
	// -------------------------------------------- //
	// IS DUPLICATE DEATH EVENT
	// -------------------------------------------- //
	// Some times when players die the PlayerDeathEvent is fired twice.
	// We want to ignore the extra calls.
	
	private static Map<UUID, PlayerDeathEvent> idToDeathEvent = new HashMap<UUID, PlayerDeathEvent>();
	
	public static boolean isDuplicateDeathEvent(PlayerDeathEvent event)
	{
		// Get the id ...
		final UUID id = event.getEntity().getUniqueId();
		
		// ... get current ...
		PlayerDeathEvent current = idToDeathEvent.get(id);
		
		// ... object equality ...
		if (current != null) return current != event;
		
		// ... store ... 
		idToDeathEvent.put(id, event);
		
		// ... and return.
		return false;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void isDuplicateDeathEventLowest(PlayerDeathEvent event)
	{
		isDuplicateDeathEvent(event);
	}
	
	// -------------------------------------------- //
	// DUPLICATE DAMAGE EVENT
	// -------------------------------------------- //
	// An entity damage by entity event is considered to be a duplicate if the damager already damaged the damagee this tick.
	
	private static Map<String, EntityDamageByEntityEvent> idToDamageEvent = new HashMap<String, EntityDamageByEntityEvent>();
	
	public static boolean isDuplicateDamageEvent(EntityDamageByEntityEvent event)
	{
		// Get the id ...
		Entity damager = MUtil.getLiableDamager(event);
		Entity damagee = event.getEntity();
		if (damager == null) return false;
		if (damagee == null) return false;
		final String id = damager.getUniqueId().toString() + damagee.getUniqueId().toString();
		
		// ... get current ...
		EntityDamageByEntityEvent current = idToDamageEvent.get(id);
		
		// ... object equality ...
		if (current != null) return current != event;
		
		// ... store ... 
		idToDamageEvent.put(id, event);
		
		// ... and return.
		return false;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void isDuplicateDamageEventLowest(EntityDamageByEntityEvent event)
	{
		isDuplicateDamageEvent(event);
	}
	
	// -------------------------------------------- //
	// DUPLICATE ARM SWING
	// -------------------------------------------- //
	// An entity damage by entity event is considered to be a duplicate if the damager already damaged the damagee this tick.
	
	private static Map<UUID, PlayerAnimationEvent> idToArmSwingEvent = new HashMap<UUID, PlayerAnimationEvent>();
	
	public static boolean isDuplicateArmSwingEvent(PlayerAnimationEvent event)
	{
		// Must be arm swing ...
		if (event.getAnimationType() != PlayerAnimationType.ARM_SWING) return false;
		
		// Get the id ...
		final UUID id = event.getPlayer().getUniqueId();
		
		// ... get current ...
		PlayerAnimationEvent current = idToArmSwingEvent.get(id);
		
		// ... object equality ...
		if (current != null) return current != event;
		
		// ... store ... 
		idToArmSwingEvent.put(id, event);
		
		// ... and return.
		return false;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void isDuplicateArmSwingEventLowest(PlayerAnimationEvent event)
	{
		isDuplicateArmSwingEvent(event);
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
		EventMassiveCorePlayerUpdate event = new EventMassiveCorePlayerUpdate(player);
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
	public void update(EventMassiveCoreAfterPlayerTeleport event)
	{
		// If we have a player ...
		Player player = event.getPlayer();
		
		// ... and the player is alive ...
		if (player.isDead()) return;
		
		// ... trigger.
		update(player);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void update(EventMassiveCoreAfterPlayerRespawn event)
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
		Damageable d = (Damageable) player;
		return d.getMaxHealth();
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
