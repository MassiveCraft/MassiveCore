package com.massivecraft.mcore.util;

import java.io.File;
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
import java.util.regex.Pattern;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.minecraft.server.v1_5_R2.DedicatedServer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_5_R2.CraftServer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.massivecraft.mcore.InternalListener;
import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.WorldNameSetEngine;
import com.massivecraft.mcore.util.extractor.Extractor;
import com.massivecraft.mcore.util.extractor.ExtractorPlayer;
import com.massivecraft.mcore.util.extractor.ExtractorPlayerName;
import com.massivecraft.mcore.util.extractor.ExtractorSender;
import com.massivecraft.mcore.util.extractor.ExtractorSenderId;
import com.massivecraft.mcore.util.extractor.ExtractorWorld;
import com.massivecraft.mcore.util.extractor.ExtractorWorldName;

public class MUtil
{
	// -------------------------------------------- //
	// IS PLAYER ID
	// -------------------------------------------- //
	
	// Player id pattern, the regex for a valid minecraft username.
	public final static Pattern playerNamePattern = Pattern.compile("^[a-zA-Z0-9_]{2,16}$");
	
	public static boolean isValidPlayerName(Object o)
	{
		if (!(o instanceof String)) return false;
		return playerNamePattern.matcher((String) o).matches();
	}
	
	// -------------------------------------------- //
	// PLAYER DIRECTORY
	// -------------------------------------------- //
	// A way of getting the player directory even when the server just started
	
	public static File getPlayerDirectory()
	{
		CraftServer cserver = (CraftServer)Bukkit.getServer();
		DedicatedServer dserver = (DedicatedServer)cserver.getServer();
		String levelName = dserver.propertyManager.getString("level-name", "world");
		return new File(Bukkit.getWorldContainer(), new File(levelName, "players").getPath());
	}
	
	public static List<String> getPlayerDirectoryNames()
	{
		List<String> ret = new ArrayList<String>();
		
		// Get the player directory
		File playerDirectory = getPlayerDirectory();
		
		// List the files in the player folder
		File[] playerfiles = playerDirectory.listFiles();
		
		// The player directory may not exist yet
		if (playerfiles == null) return ret;
		
		// Populate by removing .dat
		for (File playerfile : playerfiles)
		{
			String filename = playerfile.getName();
			String playername = filename.substring(0, filename.length()-4);
			ret.add(playername);
		}
		
		return ret;
	}
	
	// -------------------------------------------- //
	// PICK
	// -------------------------------------------- //
	
	public static <T> T regexPickFirstVal(String input, Map<String, T> regex2val)
	{
		if (regex2val == null) return null;
		T ret = null;
		
		for (Entry<String, T> entry : regex2val.entrySet())
		{
			ret = entry.getValue();
			if (input == null) continue;
			String regex = entry.getKey();
			if (Pattern.matches(regex, input)) break;
		}
		
		return ret;
	}
	
	public static <E, T> T equalsPickFirstVal(E input, Map<E, T> thing2val)
	{
		if (thing2val == null) return null;
		T ret = null;
		
		for (Entry<E, T> entry : thing2val.entrySet())
		{
			ret = entry.getValue();
			if (input == null) continue;
			E thing = entry.getKey();
			if (MUtil.equals(input, thing)) break;
		}
		
		return ret;
	}
	
	// -------------------------------------------- //
	// LOCATIONS COMPARISON
	// -------------------------------------------- //
	
	public static boolean isSameBlock(PlayerMoveEvent event)
	{
		return isSameBlock(event.getFrom(), event.getTo());
	}
	
	public static boolean isSameBlock(Location one, Location two)
	{
		if (one.getBlockX() != two.getBlockX()) return false;
		if (one.getBlockZ() != two.getBlockZ()) return false;
		if (one.getBlockY() != two.getBlockY()) return false;
		return one.getWorld().equals(two.getWorld());
	}
	
	public static boolean isSameChunk(PlayerMoveEvent event)
	{
		return isSameChunk(event.getFrom(), event.getTo());
	}
	
	public static boolean isSameChunk(Location one, Location two)
	{
		if (one.getBlockX() >> 4 != one.getBlockX() >> 4) return false;
		if (one.getBlockZ() >> 4 != one.getBlockZ() >> 4) return false;
		return one.getWorld().equals(two.getWorld());
	}
	
	// -------------------------------------------- //
	// FACE AND YAW
	// -------------------------------------------- //
	
	public static Float getYaw(BlockFace face)
	{
		switch (face)
		{
			case NORTH: return 0f;
			case EAST: return 90f;
			case SOUTH: return 180f;
			case WEST: return 270f;
			case UP: return null;
			case DOWN: return null;
			case NORTH_EAST: return 45f;
			case NORTH_WEST: return 315f;
			case SOUTH_EAST: return 135f;
			case SOUTH_WEST: return 225f;
			case WEST_NORTH_WEST: return 292.5f;
			case NORTH_NORTH_WEST: return 337.5f;
			case NORTH_NORTH_EAST: return 22.5f;
			case EAST_NORTH_EAST: return 67.5f;
			case EAST_SOUTH_EAST: return 112.5f;
			case SOUTH_SOUTH_EAST: return 157.5f;
			case SOUTH_SOUTH_WEST: return 202.5f;
			case WEST_SOUTH_WEST: return 247.5f;
			case SELF: return null;
		}
		return null;
	}
	
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
	// WORLD DERP
	// -------------------------------------------- //
	
	public static Set<String> getLoadedWorldNames()
	{
		return WorldNameSetEngine.get().getWorldNames();
	}
	
	// -------------------------------------------- //
	// SIMPLE CONSTRUCTORS
	// -------------------------------------------- //
	
	
	@SafeVarargs
	public static <T> List<T> list(T... items)
	{
		return new ArrayList<T>(Arrays.asList(items));
	}
	
	@SafeVarargs
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
	// LE NICE EQUALS and compare
	// -------------------------------------------- //
	
	public static boolean equals(Object herp, Object derp)
	{
		if (herp == null) return derp == null;
		if (derp == null) return herp == null;
		return herp.equals(derp);
	}
	
	public static <T> int compare(Comparable<T> herp, T derp)
	{
		if (herp == null && derp == null) return 0;
		if (herp == null) return -1;
		if (derp == null) return +1;
		return herp.compareTo(derp);
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
		registerExtractor(CommandSender.class, "sender", ExtractorSender.get());
		registerExtractor(String.class, "senderId", ExtractorSenderId.get());
		
		registerExtractor(Player.class, "player", ExtractorPlayer.get());
		registerExtractor(String.class, "playerName", ExtractorPlayerName.get());
		
		registerExtractor(World.class, "world", ExtractorWorld.get());
		registerExtractor(String.class, "worldName", ExtractorWorldName.get());
	}
}
