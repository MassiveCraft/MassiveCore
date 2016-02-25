package com.massivecraft.massivecore.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import com.massivecraft.massivecore.Engine;

public class PlayerUtil extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static PlayerUtil i = new PlayerUtil();
	public static PlayerUtil get() { return i; }
	public PlayerUtil()
	{
		this.setPeriod(1L);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void setActiveInner(boolean active)
	{
		if ( ! active) return;
		
		idToDeathEvent.clear();
		idToDamageEvent.clear();
		idToArmSwingEvent.clear();
		
		idToLastMoveMillis.clear();
	}
	
	@Override
	public void run()
	{
		idToDeathEvent.clear();
		idToDamageEvent.clear();
		idToArmSwingEvent.clear();
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
		if (MUtil.isntPlayer(player)) return;
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
		if (MUtil.isntPlayer(player)) return 0;
		Long ret = idToLastDamageMillis.get(player.getUniqueId());
		if (ret == null) return 0;
		return ret;
	}
	
	public static long getNoDamageMillis(Player player)
	{
		if (MUtil.isntPlayer(player)) return 0;
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
		Player player = event.getPlayer();
		if (MUtil.isntPlayer(player)) return false;
		final UUID id = player.getUniqueId();
		
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
	
}
