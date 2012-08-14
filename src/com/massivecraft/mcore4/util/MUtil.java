package com.massivecraft.mcore4.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;

import com.massivecraft.mcore4.MCore;

public class MUtil
{
	// -------------------------------------------- //
	// MATERIAL FACTS
	// -------------------------------------------- //
	
	public final static Set<Material> foodMaterials = new HashSet<Material>(MUtil.list(
		Material.APPLE,
		Material.BREAD,
		Material.COOKED_BEEF,
		Material.COOKED_CHICKEN,
		Material.COOKED_FISH,
		Material.COOKIE,
		Material.GRILLED_PORK,
		Material.GOLDEN_APPLE,
		Material.MELON,
		Material.MUSHROOM_SOUP,
		Material.PORK,
		Material.RAW_BEEF,
		Material.RAW_CHICKEN,
		Material.RAW_FISH,
		Material.ROTTEN_FLESH,
		Material.SPIDER_EYE
	));
	
	// -------------------------------------------- //
	// EVENT DERP
	// -------------------------------------------- //
	
	public static Material getEatenMaterial(PlayerInteractEvent event)
	{
		Action action = event.getAction();
		if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return null;
		
		Material ret = null;
		
		if (action == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.CAKE_BLOCK)
		{
			ret = Material.CAKE_BLOCK;
		}
		else if (foodMaterials.contains(event.getMaterial()))
		{
			ret = event.getMaterial();
		}
		
		return ret;
	}
	
	public static boolean isCombatEvent(EntityDamageEvent event)
	{
		if (event.getCause() != DamageCause.ENTITY_ATTACK && event.getCause() != DamageCause.PROJECTILE) return false;
		return event instanceof EntityDamageByEntityEvent;
	}
	
	public static boolean isCloseCombatEvent(EntityDamageEvent event)
	{
		if (event.getCause() != DamageCause.ENTITY_ATTACK) return false;
		return event instanceof EntityDamageByEntityEvent;
	}
	
	public static Entity getLiableDamager(EntityDamageEvent event)
	{
		if ( ! (event instanceof EntityDamageByEntityEvent)) return null;
		EntityDamageByEntityEvent edbeEvent = (EntityDamageByEntityEvent)event;
		Entity ret = edbeEvent.getDamager();
		if (ret instanceof Projectile)
		{
			ret = ((Projectile)ret).getShooter();
		}
		return ret;
	}
	
	// -------------------------------------------- //
	// SIMPLE CONSTRUCTORS
	// -------------------------------------------- //
	
	public static <T> List<T> list(T... items)
	{
		return new ArrayList<T>(Arrays.asList(items));
	}
	
	public static <T> Set<T> set(T... items)
	{
		return new LinkedHashSet<T>(Arrays.asList(items));
	}
	
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> map(K key1, V value1, Object... objects)
	{
		Map<K, V> ret = new LinkedHashMap<K, V>();
		
		ret.put(key1, value1);
		
		Iterator<Object> iter = Arrays.asList(objects).iterator();
		while (iter.hasNext())
		{
			K key = (K) iter.next();
			V value = (V) iter.next();
			ret.put(key, value);
		}
		
		return ret;
	}
	
	// -------------------------------------------- //
	// SORTING
	// -------------------------------------------- //
	
	//http://stackoverflow.com/questions/2864840/treemap-sort-by-value
	public static <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
	    SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
	        new Comparator<Map.Entry<K,V>>() {
	            @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
	            	int res = e1.getValue().compareTo(e2.getValue());
	                return res != 0 ? res : 1; // Special fix to preserve items with equal values
	            }
	        }
	    );
	    sortedEntries.addAll(map.entrySet());
	    return sortedEntries;
	}
	
	// -------------------------------------------- //
	// MATH
	// -------------------------------------------- //
	public static <T extends Number> T limitNumber(T d, T min, T max)
	{
		if (min instanceof Number && d.doubleValue() < min.doubleValue())
		{
			return min;
		}
		
		if (max instanceof Number && d.doubleValue() > max.doubleValue())
		{
			return max;
		}
		
		return d;
	}
	
	public static long probabilityRound(double val)
	{
		long ret = (long) Math.floor(val);
		double prob = val % 1;
		if (MCore.random.nextDouble() < prob) ret += 1;
		return ret;
	}
}
