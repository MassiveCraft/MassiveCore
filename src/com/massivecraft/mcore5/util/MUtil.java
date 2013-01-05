package com.massivecraft.mcore5.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.mcore5.InternalListener;
import com.massivecraft.mcore5.MCore;
import com.massivecraft.mcore5.util.extractor.Extractor;
import com.massivecraft.mcore5.util.extractor.ExtractorPlayer;
import com.massivecraft.mcore5.util.extractor.ExtractorPlayerName;
import com.massivecraft.mcore5.util.extractor.ExtractorWorld;
import com.massivecraft.mcore5.util.extractor.ExtractorWorldName;

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
	
	// Note that this one is unstable and invalid. It cannot catch all cases.
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
	
	/**
	 * This method will return the ItemStack the player is trying to equip.
	 * If the click event would not result in equipping something null will be returned.
	 * Note that this algorithm is not perfect. It's an adequate guess.
	 * 
	 * @param event The InventoryClickEvent to analyze.
	 * @return The ItemStack the player is trying to equip.
	 */
	public static ItemStack getEquipping(InventoryClickEvent event)
	{
		/*
		System.out.println("---");
		System.out.println("getInventory().getType() "+event.getInventory().getType());
		System.out.println("getView().getTopInventory().getType() "+event.getView().getTopInventory().getType());
		System.out.println("getView().getType() "+event.getView().getType());
		System.out.println("getView().getBottomInventory().getType() "+event.getView().getBottomInventory().getType());
		System.out.println("event.getSlotType() "+event.getSlotType());
		System.out.println("event.getRawSlot() "+event.getRawSlot());
		System.out.println("event.getSlot() "+event.getSlot());
		*/
		
		boolean isShiftClick = event.isShiftClick();
		InventoryType inventoryType = event.getInventory().getType();
		SlotType slotType = event.getSlotType();
		ItemStack cursor = event.getCursor();
		ItemStack currentItem = event.getCurrentItem();
		
		if (isShiftClick)
		{
			if (inventoryType != InventoryType.CRAFTING) return null;
			if (slotType == SlotType.CRAFTING) return null;
			if (slotType == SlotType.ARMOR) return null;
			if (slotType == SlotType.RESULT) return null;
			if (currentItem.getType() == Material.AIR) return null;
			return currentItem;
		}
		else
		{
			if (slotType == SlotType.ARMOR)
			{
				return cursor;
			}
			return null;
		}
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
	
	public static String kickReason(PlayerQuitEvent event)
	{
		return InternalListener.kickedPlayerReasons.get(event.getPlayer().getName());
	}
	
	public static boolean causedByKick(PlayerQuitEvent event)
	{
		return kickReason(event) != null;
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
	
	public static <K, V> Map<V, K> flippedMap(Map<K, V> map)
	{
		Map<V, K> ret = new LinkedHashMap<V, K>();
		
		for(Entry<K, V> entry : map.entrySet())
		{
			V value = entry.getValue();
			K key = entry.getKey();
			
			if (value == null) continue;
			ret.put(value, key);
		}
		
		return ret;
	}
	
	// -------------------------------------------- //
	// LE NICE RANDOM
	// -------------------------------------------- //
	
	public static <T> T random(Collection<T> coll)
	{
		if (coll.size() == 0) return null;
		if (coll.size() == 1) return coll.iterator().next();
		int index = MCore.random.nextInt(coll.size());
		return new ArrayList<T>(coll).get(index);
	}
	
	// -------------------------------------------- //
	// LE NICE EQUALS
	// -------------------------------------------- //
	
	public static boolean equals(Object herp, Object derp)
	{
		if (herp == null && derp == null)
		{
			return true;
		}
		
		if (herp == null || derp == null)
		{
			return false;
		}
		
		return herp.equals(derp);
	}
	
	// -------------------------------------------- //
	// SORTING
	// -------------------------------------------- //
	
	//http://stackoverflow.com/questions/2864840/treemap-sort-by-value
	/*public static <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
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
	}*/
	
	// http://stackoverflow.com/questions/2864840/treemap-sort-by-value
	public static <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map)
	{
		return entriesSortedByValues(map, true);
	}
	
	public static <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map, final boolean ascending)
	{
		SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
			new Comparator<Map.Entry<K,V>>()
			{
				@Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2)
				{
					int res;
					if (ascending)
					{
						res = e1.getValue().compareTo(e2.getValue());
					}
					else
					{
						res = e2.getValue().compareTo(e1.getValue());
					}
					return res != 0 ? res : 1;
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
	
	// -------------------------------------------- //
	// EXTRACTION
	// -------------------------------------------- //
	
	protected static Map<Class<?>, Map<String, Set<Extractor>>> classesPropertiesExtractors = new HashMap<Class<?>, Map<String, Set<Extractor>>>();
	protected static Map<String, Set<Extractor>> getPropertiesExtractors(Class<?> propertyClass)
	{
		Map<String, Set<Extractor>> ret = classesPropertiesExtractors.get(propertyClass);
		if (ret == null)
		{
			ret = new HashMap<String, Set<Extractor>>();
			classesPropertiesExtractors.put(propertyClass, ret);
		}
		return ret;
	}
	
	protected static Set<Extractor> getExtractors(Class<?> propertyClass, String propertyName)
	{
		Map<String, Set<Extractor>> propertiesExtractors = getPropertiesExtractors(propertyClass);
		Set<Extractor> ret = propertiesExtractors.get(propertyName);
		if (ret == null)
		{
			ret = new HashSet<Extractor>();
			propertiesExtractors.put(propertyName, ret);
		}
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T extract(Class<T> propertyClass, String propertyName, Object o)
	{
		Object ret = null;
		for (Extractor extractor : getExtractors(propertyClass, propertyName))
		{
			ret = extractor.extract(o);
			if (ret != null) break;
		}
		return (T) ret;
	}
	
	public static <T> void registerExtractor(Class<T> clazz, String propertyName, Extractor extractor)
	{
		getExtractors(clazz, propertyName).add(extractor);
	}
	
	// -------------------------------------------- //
	// STATIC CONSTRUCT
	// -------------------------------------------- //
	
	static
	{
		registerExtractor(World.class, "world", new ExtractorWorld());
		registerExtractor(String.class, "worldName", new ExtractorWorldName());
		registerExtractor(Player.class, "player", new ExtractorPlayer());
		registerExtractor(String.class, "playerName", new ExtractorPlayerName());
	}
}
