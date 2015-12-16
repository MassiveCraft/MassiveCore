package com.massivecraft.massivecore.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;
import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.event.EventMassiveCoreAfterPlayerRespawn;
import com.massivecraft.massivecore.event.EventMassiveCoreAfterPlayerTeleport;
import com.massivecraft.massivecore.event.EventMassiveCorePlayerUpdate;
import com.massivecraft.massivecore.util.MUtil;

/**
 * This event triggers the EventMassiveCorePlayerUpdate on every block change.
 * It also runs it in reset mode rather than update mode upon world change.
 */
public class EngineMassiveCorePlayerUpdate extends EngineAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineMassiveCorePlayerUpdate i = new EngineMassiveCorePlayerUpdate();
	public static EngineMassiveCorePlayerUpdate get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Plugin getPlugin()
	{
		return MassiveCore.get();
	}
	
	// -------------------------------------------- //
	// UPDATE
	// -------------------------------------------- //
	
	public static void update(Player player, boolean current)
	{
		// If this player is actually a player and not an NPC ...
		if (MUtil.isntPlayer(player)) return;
		
		// ... and the player is alive ...
		if (player.isDead()) return;
		
		// ... store data for no cheat plus bug fix ...
		if (player.isFlying())
		{
			setLastFlyActive(player, System.currentTimeMillis());
		}
		
		// ... then trigger an update.
		EventMassiveCorePlayerUpdate.run(player, current);
	}
	
	// NOTE: Can't be cancelled
	@EventHandler(priority = EventPriority.LOWEST)
	public void update(PlayerJoinEvent event)
	{
		update(event.getPlayer(), true);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void update(EventMassiveCoreAfterPlayerTeleport event)
	{
		update(event.getPlayer(), true);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void update(EventMassiveCoreAfterPlayerRespawn event)
	{
		update(event.getPlayer(), true);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void update(PlayerChangedWorldEvent event)
	{
		update(event.getPlayer(), true);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void update(PlayerMoveEvent event)
	{
		// Only on block change!
		if (MUtil.isSameBlock(event)) return;
		
		update(event.getPlayer(), true);
	}
	
	// -------------------------------------------- //
	// FIX NO CHEAT PLUS BUG
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
	public void fixNoCheatPlusBug(EntityDamageEvent event)
	{
		// If a player ...
		if (MUtil.isntPlayer(event.getEntity())) return;
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
	
}
