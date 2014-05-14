package com.massivecraft.mcore.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import com.massivecraft.mcore.EngineMainMCore;
import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.EngineWorldNameSet;
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
	// IS VALID PLAYER NAME
	// -------------------------------------------- //
	
	// The regex for a valid minecraft player name.
	public final static Pattern playerNamePattern = Pattern.compile("^[a-zA-Z0-9_]{2,16}$");
	
	public static boolean isValidPlayerName(String string)
	{
		return playerNamePattern.matcher(string).matches();
	}
	
	// -------------------------------------------- //
	// IS VALID UUID
	// -------------------------------------------- //
	
	public static boolean isValidUUID(String string)
	{
		if (string == null) return false;
		try
		{
			UUID.fromString(string);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	// -------------------------------------------- //
	// STACK TRACE STRING
	// -------------------------------------------- //
	
	public static String getStackTraceString()
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		new Throwable().printStackTrace(pw);
		return sw.toString();
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
	
	public static <T> T recurseResolveMap(T input, Map<T, T> map)
	{
		T output = map.get(input);
		if (output == null) return input;
		return recurseResolveMap(output, map);
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
		if (one.getBlockX() >> 4 != two.getBlockX() >> 4) return false;
		if (one.getBlockZ() >> 4 != two.getBlockZ() >> 4) return false;
		if (one.getWorld() != two.getWorld()) return false;
		return true;
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
			Projectile projectile = (Projectile)ret;
			ProjectileSource projectileSource = projectile.getShooter();
			if (projectileSource instanceof Entity)
			{
				ret = (Entity)projectileSource;
			}
		}
		return ret;
	}
	
	public static String kickReason(PlayerQuitEvent event)
	{
		return EngineMainMCore.kickedPlayerReasons.get(event.getPlayer().getName());
	}
	
	public static boolean causedByKick(PlayerQuitEvent event)
	{
		return kickReason(event) != null;
	}
	
	// -------------------------------------------- //
	// POTION DERP
	// -------------------------------------------- //
	
	/**
	 * Get just the potion effect bits. This is to work around bugs with potion parsing.
	 * Workaround created by the WorldGuard team: https://github.com/sk89q/worldguard/commit/8dec32fa6a1238a11743cea8b8302a6c9d2aaa55
	 * This issue is reported as BUKKIT-4612 "Potion.fromItemStack causes IllegalArgumentException: Instant potions cannot be extended"
	 *
	 * @param item item
	 * @return new bits
	 */
	public static int getPotionEffectBits(ItemStack item)
	{
		return item.getDurability() & 0x3F;
	}
	
	/**
	 * Checks if the given potion is a vial of water.
	 *
	 * @param item the item to check
	 * @return true if it's a water vial
	 */
	public static boolean isWaterPotion(ItemStack item)
	{
		return getPotionEffectBits(item) == 0;
	}
	
	@SuppressWarnings("deprecation")
	public static List<PotionEffect> getPotionEffects(ItemStack itemStack)
	{
		if (itemStack == null) return null;
		if (itemStack.getType() != Material.POTION) return null;

		List<PotionEffect> ret = new ArrayList<PotionEffect>();
		
		if (isWaterPotion(itemStack)) return ret;
		
		Potion potion = Potion.fromDamage(getPotionEffectBits(itemStack));
		ret.addAll(potion.getEffects());
		
		PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
		if (meta.hasCustomEffects())
		{
			ret.addAll(meta.getCustomEffects());
		}
		
		return ret;
	}
	
	public static final Set<PotionEffectType> HARMFUL_POTION_EFFECTS = Collections.unmodifiableSet(MUtil.set(
		PotionEffectType.BLINDNESS,
		PotionEffectType.CONFUSION,
		PotionEffectType.HARM,
		PotionEffectType.HUNGER,
		PotionEffectType.POISON,
		PotionEffectType.SLOW,
		PotionEffectType.SLOW_DIGGING,
		PotionEffectType.WEAKNESS,
		PotionEffectType.WITHER
	));
	
	public static boolean isHarmfulPotion(PotionEffectType potionEffectType)
	{
		return HARMFUL_POTION_EFFECTS.contains(potionEffectType);
	}
	
	public static boolean isHarmfulPotion(PotionEffect potionEffect)
	{
		if (potionEffect == null) return false;
		return isHarmfulPotion(potionEffect.getType());
	}
	
	public static boolean isHarmfulPotion(ItemStack itemStack)
	{
		List<PotionEffect> potionEffects = getPotionEffects(itemStack);
		if (potionEffects == null) return false;
		
		for (PotionEffect potionEffect : potionEffects)
		{
			if (isHarmfulPotion(potionEffect)) return true;
		}
		return false;
	}
	
	public static boolean isHarmfulPotion(ThrownPotion thrownPotion)
	{
		return isHarmfulPotion(thrownPotion.getItem());
	}
	
	// -------------------------------------------- //
	// WORLD DERP
	// -------------------------------------------- //
	
	public static Set<String> getLoadedWorldNames()
	{
		return EngineWorldNameSet.get().getWorldNames();
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
	// COLLECTION MANIPULATION
	// -------------------------------------------- //
	
	public static <T> T removeByIndex(Collection<T> coll, int index)
	{
		if (coll == null) throw new NullPointerException("coll");
		
		if (coll instanceof List<?>)
		{
			return ((List<T>)coll).remove(index);
		}
		
		if (index < 0) throw new IndexOutOfBoundsException("index < 0");
		if (index >= coll.size()) throw new IndexOutOfBoundsException("index > collection size");
		
		int i = -1;
		Iterator<T> iter = coll.iterator();
		while (iter.hasNext())
		{
			i++;
			T ret = iter.next();
			if (i != index) continue;
			iter.remove();
			return ret;
		}
		
		return null;
	}
	
	// -------------------------------------------- //
	// LE NICE RANDOM
	// -------------------------------------------- //
	
	public static <T> T random(Collection<T> coll)
	{
		if (coll.size() == 0) return null;
		if (coll.size() == 1) return coll.iterator().next();
		int index = MCore.random.nextInt(coll.size());
		List<T> list = null;
		if (coll instanceof List<?>)
		{
			list = (List<T>)coll;
		}
		else
		{
			list = new ArrayList<T>(coll);
		}
		return list.get(index);
	}
	
	public static <T> List<T> random(Collection<T> coll, int count)
	{
		List<T> ret = new ArrayList<T>(coll);
		if (count < 0) count = 0;
		while (ret.size() > count)
		{
			int index = MCore.random.nextInt(ret.size());
			ret.remove(index);
		}
		return ret;
	}
	
	// -------------------------------------------- //
	// LE NICE EQUALS and compare
	// -------------------------------------------- //
	
	public static boolean equals(Object herp, Object derp)
	{
		if (herp == null) return derp == null;
		if (derp == null) return false;
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
		
		registerExtractor(String.class, "accountId", ExtractorSenderId.get());
	}
	
}
