package com.massivecraft.massivecore.engine;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.WeakHashMap;

public class EngineMassiveCoreGank extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineMassiveCoreGank i = new EngineMassiveCoreGank();
	public static EngineMassiveCoreGank get() { return i; }
	
	// -------------------------------------------- //
	// PROTECTED
	// -------------------------------------------- //
	
	// NOTE: The usage of WeakHashMap here is important. We would create a memory leak otherwise. WeakHashMap works very well for meta data storage.
	protected WeakHashMap<Entity, WeakHashMap<Player, Double>> entityToPlayerDamages = new WeakHashMap<>();
	
	protected Map<Player, Double> getPlayerDamages(Entity entity, boolean store)
	{
		WeakHashMap<Player, Double> ret = this.entityToPlayerDamages.get(entity);
		
		if (ret == null)
		{
			ret = new WeakHashMap<>(4);
			if (store)
			{
				this.entityToPlayerDamages.put(entity, ret);
			}
		}
		
		return ret;
	}
	
	// -------------------------------------------- //
	// PUBLIC
	// -------------------------------------------- //
	
	public Map<Player, Double> getPlayerDamages(Entity entity)
	{
		return this.getPlayerDamages(entity, false);
	}
	
	public Set<Player> getPlayers(Entity entity)
	{
		return this.getPlayerDamages(entity).keySet();
	}
	
	public Map<Player, Double> getPlayerQuotients(Entity entity)
	{
		// Get PlayerDamages
		Map<Player, Double> playerDamages = getPlayerDamages(entity);
		
		// Calculate Total
		double total = 0;
		for (Double damage : playerDamages.values())
		{
			total += damage;
		}
		
		// Create Ret
		Map<Player, Double> ret = new MassiveMap<>(playerDamages.size());
		
		// Fill Ret
		for (Entry<Player, Double> playerDamage : playerDamages.entrySet())
		{
			Player player = playerDamage.getKey();
			Double damage = playerDamage.getValue();
			ret.put(player, damage / total);
		}
		
		// Return Ret
		return ret;
	}
	
	// -------------------------------------------- //
	// LISTENER
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityDamage(EntityDamageByEntityEvent event)
	{
		// If an entity ...
		Entity entity = event.getEntity();
		
		// ... takes damage ...
		double damage = event.getDamage();
		if (damage <= 0.1) return;
		
		// ... and the damager ...
		Entity edamager = MUtil.getLiableDamager(event);
		
		// ... is a player ...
		if (MUtil.isntPlayer(edamager)) return;
		Player player = (Player)edamager;
		
		// ... that is someone else ...
		if (player.equals(entity)) return;
		
		// ... then get player damages ...
		Map<Player, Double> playerDamages = this.getPlayerDamages(entity, true);
		
		// ... get player damage ...
		Double playerDamage = playerDamages.get(player);
		if (playerDamage == null) playerDamage = 0D;
		
		// ... increment ...
		playerDamage += damage;
		
		// ... and set.
		playerDamages.put(player, playerDamage);
	}
	
}
