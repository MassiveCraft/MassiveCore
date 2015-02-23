package com.massivecraft.massivecore.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import com.massivecraft.massivecore.CaseInsensitiveComparator;
import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveCoreEngineMain;
import com.massivecraft.massivecore.MassiveCoreEngineWorldNameSet;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.collections.MassiveTreeSet;
import com.massivecraft.massivecore.util.extractor.Extractor;
import com.massivecraft.massivecore.util.extractor.ExtractorPlayer;
import com.massivecraft.massivecore.util.extractor.ExtractorPlayerName;
import com.massivecraft.massivecore.util.extractor.ExtractorSender;
import com.massivecraft.massivecore.util.extractor.ExtractorSenderId;
import com.massivecraft.massivecore.util.extractor.ExtractorSenderName;
import com.massivecraft.massivecore.util.extractor.ExtractorWorld;
import com.massivecraft.massivecore.util.extractor.ExtractorWorldName;

public class MUtil
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	private static Method methodGetOnlinePlayers;
	
	static
	{
		methodGetOnlinePlayers = getMethodGetOnlinePlayers();
	}
	
	// -------------------------------------------- //
	// GET ONLINE PLAYERS
	// -------------------------------------------- //
	// It seems we can not always trust the Bukkit.getOnlinePlayers() method.
	// Due to compilation issue this method might not exist in the form we compiled against.
	// Spigot 1.8 and the 1.7 Bukkit might have been compiled slightly differently resulting in this issue.
	// Issue Example: https://github.com/MassiveCraft/MassiveCore/issues/192
	
	public static Method getMethodGetOnlinePlayers()
	{
		Method ret = null;
		
		try
		{
			for (Method method : Bukkit.class.getDeclaredMethods())
			{
				// The method name must be getOnlinePlayers ...
				if ( ! method.getName().equals("getOnlinePlayers")) continue;
				
				// ... if we find such a method it's better than nothing ...
				if (ret == null) ret = method;
				
				// ... but if the method additionally returns a collection ...
				if ( ! method.getReturnType().isAssignableFrom(Collection.class)) continue;
				
				// ... that is preferable ...
				ret = method;
				
				// ... and we need not look any further.
				break;
			}
			
			ret.setAccessible(true);
		}
		catch (Exception e)
		{
			// If we fail we do so silently.
			// This method is probably almost never going to be used anyways.
		}
		
		return ret;
	}
	
	public static Collection<? extends Player> getOnlinePlayers()
	{
		// Fetch some kind of playersObject.
		Object playersObject = null;
		try
		{
			playersObject = Bukkit.getOnlinePlayers();
		}
		catch (Throwable t)
		{
			// That didn't work!
			// We probably just caught a NoSuchMethodError.
			// So let's try with reflection instead.
			try
			{
				playersObject = methodGetOnlinePlayers.invoke(null);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		// Now return the playersObject.
		if (playersObject instanceof Collection<?>)
		{
			@SuppressWarnings("unchecked")
			Collection<? extends Player> playersCollection = (Collection<? extends Player>)playersObject;
			return playersCollection;
		}
		else if (playersObject instanceof Player[])
		{
			Player[] playersArray = (Player[])playersObject;
			return Arrays.asList(playersArray);
		}
		else
		{
			throw new RuntimeException("Failed retrieving online players.");
		}
	}
	
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
	// STACK TRACE: GET
	// -------------------------------------------- //
	
	public static List<StackTraceElement> getStackTrace(Thread thread, int skip)
	{
		// Cut away this method.
		// Cut away system line.
		// We must check if it's the current thread.
		if (thread.equals(Thread.currentThread())) skip += 2;
		
		StackTraceElement[] elements = thread.getStackTrace();
		elements = Arrays.copyOfRange(elements, skip, elements.length);
		return new ArrayList<StackTraceElement>(Arrays.asList(elements));
	}
	
	public static List<StackTraceElement> getStackTrace(Thread thread)
	{
		int skip = 0;
		
		// Cut away this method.
		// We must check if it's the current thread.
		if (thread.equals(Thread.currentThread())) skip += 1;
		
		return getStackTrace(thread, skip);
	}
	
	public static List<StackTraceElement> getStackTrace(int skip)
	{
		// Cut away this method.
		// We already know it's going to be the current thread.
		skip++;
		
		Thread thread = Thread.currentThread();
		
		return getStackTrace(thread, skip);
	}
	
	public static List<StackTraceElement> getStackTrace()
	{
		// Cut away this method.
		// We already know it's going to be the current thread.
		int skip = 1;
		
		Thread thread = Thread.currentThread();
		
		return getStackTrace(thread, skip);
	}
	
	// -------------------------------------------- //
	// STACK TRACE STRING: MANY
	// -------------------------------------------- //
	
	public static List<String> getStackTraceStrings(List<StackTraceElement> stackTrace, boolean color)
	{
		List<String> ret = new MassiveList<String>();
		
		for (StackTraceElement element : stackTrace)
		{
			ret.add(getStackTraceString(element, color));
		}
		
		return ret;
	}
	
	public static String getStackTraceString(List<StackTraceElement> stackTrace, boolean color, String glue)
	{
		return Txt.implode(getStackTraceStrings(stackTrace, color), glue);
	}
	
	// -------------------------------------------- //
	// STACK TRACE STRING: ONE
	// -------------------------------------------- //
	
	// Same as the Java8 source but with color option.
	public static String getStackTraceString(StackTraceElement element, boolean color)
	{
		ChatColor separatorColor = ChatColor.GRAY;
		ChatColor classColor = ChatColor.YELLOW;
		ChatColor methodColor = ChatColor.GREEN;
		ChatColor fileColor = ChatColor.AQUA;
		ChatColor lineColor = ChatColor.LIGHT_PURPLE;
		
		String className = element.getClassName();
		String methodName = element.getMethodName();
		boolean nativeMethod = element.isNativeMethod();
		String fileName = element.getFileName();
		int lineNumber = element.getLineNumber();
		
		StringBuilder ret = new StringBuilder();
		
		if (color) ret.append(classColor);
		ret.append(className);
		
		if (color) ret.append(separatorColor);
		ret.append(".");
		
		if (color) ret.append(methodColor);
		ret.append(methodName);
		
		if (color) ret.append(separatorColor);
		ret.append("(");
		
		if (color) ret.append(fileColor);
		if (nativeMethod)
		{
			ret.append("Native Method");
		}
		else
		{
			if (fileName != null && lineNumber >= 0)
			{
				ret.append(fileName);
				
				if (color) ret.append(separatorColor);
				ret.append(":");
				
				if (color) ret.append(lineColor);
				ret.append(lineNumber);
			}
			else
			{
				if (fileName != null)
				{
					ret.append(fileName);
				}
				else
				{
					ret.append("Unknown Source");
				}
			}
		}
		
		if (color) ret.append(separatorColor);
		ret.append(")");
		
		return ret.toString();
	}
	
	// -------------------------------------------- //
	// LIST OPERATIONS
	// -------------------------------------------- //
	
	public static <T> List<T> repeat(T object, int times)
	{
		List<T> ret = new ArrayList<T>(times);
		for (int i = 1; i <= times; i++)
		{
			ret.add(object);
		}
		return ret;
	}
	
	public static void keepLeft(List<?> list, int maxlength)
	{
		if (list.size() <= maxlength) return;
		list.subList(maxlength, list.size()).clear();
	}
	
	public static void keepRight(List<?> list, int maxlength)
	{
		if (list.size() <= maxlength) return;
		list.subList(0, maxlength).clear();
	}
	
	public static <T> void padLeft(List<T> list, T object, int length)
	{
		if (list.size() >= length) return;
		list.addAll(0, repeat(object, length - list.size()));
	}
	
	public static <T> void padRight(List<T> list, T object, int length)
	{
		if (list.size() >= length) return;
		list.addAll(repeat(object, length - list.size()));
	}
	
	// -------------------------------------------- //
	// ITERABLE MATH
	// -------------------------------------------- //
	
	public static <T extends Number> double getSum(Iterable<T> numbers)
	{
		if (numbers == null) throw new NullPointerException("numbers");
		
		double sum = 0;
		for (T number : numbers)
		{
			sum += number.doubleValue();
		}
		
		return sum;
	}
	
	public static <T extends Number> double getAverage(Iterable<T> numbers)
	{
		if (numbers == null) throw new NullPointerException("numbers");
		
		double sum = 0;
		int count = 0;
		for (T number : numbers)
		{
			sum += number.doubleValue();
			count++;
		}
		
		if (count == 0) throw new IllegalArgumentException("numbers empty");
		
		return sum / count;
	}
	
	// -------------------------------------------- //
	// TABLE OPERATIONS
	// -------------------------------------------- //
	
	public static <T> List<List<T>> rotateLeft(List<List<T>> rows)
	{
		List<List<T>> ret = transpose(rows);
		flipVertically(ret);
		return ret;
	}
	
	public static <T> List<List<T>> rotateRight(List<List<T>> rows)
	{
		List<List<T>> ret = transpose(rows);
		flipHorizontally(ret);
		return ret;
	}
	
	public static <T> List<List<T>> transpose(List<List<T>> rows)
	{
		List<List<T>> ret = new ArrayList<List<T>>();
		
		final int n = rows.get(0).size();
		
		for (int i = 0; i < n; i++)
		{
			List<T> col = new ArrayList<T>();
			for (List<T> row : rows)
			{
				col.add(row.get(i));
			}
			ret.add(col);
		}
		
		return ret;
	}
	
	public static <T> void flipHorizontally(List<List<T>> rows)
	{
		for (List<T> row : rows)
		{
			Collections.reverse(row);
		}
	}
	
	public static <T> void flipVertically(List<List<T>> rows)
	{
		Collections.reverse(rows);
	}
	
	// -------------------------------------------- //
	// COLOR INT CODE
	// -------------------------------------------- //
	
	public static int getCode(ChatColor chatColor)
	{
		switch (chatColor)
		{
			case BLACK: return 0x00;
			case DARK_BLUE: return 0x1;
			case DARK_GREEN: return 0x2;
			case DARK_AQUA: return 0x3;
			case DARK_RED: return 0x4;
			case DARK_PURPLE: return 0x5;
			case GOLD: return 0x6;
			case GRAY: return 0x7;
			case DARK_GRAY: return 0x8;
			case BLUE: return 0x9;
			case GREEN: return 0xA;
			case AQUA: return 0xB;
			case RED: return 0xC;
			case LIGHT_PURPLE: return 0xD;
			case YELLOW: return 0xE;
			case WHITE: return 0xF;
			case MAGIC: return 0x10;
			case BOLD: return 0x11;
			case STRIKETHROUGH: return 0x12;
			case UNDERLINE: return 0x13;
			case ITALIC: return 0x14;
			case RESET: return 0x15;
		}
		throw new IllegalArgumentException("The chat color " + chatColor.name() + " is not yet supported!");
	}
	
	// -------------------------------------------- //
	// ENTITY DAMAGE EVENT
	// -------------------------------------------- //
	// The EntityDamageEvent#setDamge(double damage) is somehow broken.
	// The MAGIC modifier does not scale as one would expect it to.
	// It jumps all over the place every time the method is called.
	// And finally it has accumulated so much randomness that players may suddenly be healed instead of harmed.
	// Or they may instantly die. For this reason we take inspiration from MCMMO who rolled their own setDamage function.
	// This method sets the BASE damage modifier and scales all other modifiers proportionally.
	
	public static void setDamage(EntityDamageEvent event, double damage)
	{
		// Save Old Damage 
		final double oldDamage = event.getDamage(DamageModifier.BASE);
		
		// No Change?
		if (damage == oldDamage) return;
		
		// So what factor are we essentially applying?
		final double factor = damage / oldDamage;
		
		// Now scale all damage modifiers!
		for (DamageModifier modifier : DamageModifier.values())
		{
			// Is this modifier used in the event?
			if ( ! event.isApplicable(modifier)) continue;
			
			// Is it the base damage? Cool! Just set it directly!
			// We could have used the factor too but then we might suffer floating point degredation.
			if (modifier == DamageModifier.BASE)
			{
				event.setDamage(modifier, damage);
				continue;
			}
			
			// Apply Factor
			event.setDamage(modifier, factor * event.getDamage(modifier));
		}
	}
	
	// Same as above but scales directly.
	public static void scaleDamage(EntityDamageEvent event, double factor)
	{
		// No Change?
		if (factor == 1) return;
		
		for (DamageModifier modifier : DamageModifier.values())
		{
			// Is this modifier used in the event?
			if ( ! event.isApplicable(modifier)) continue;
			
			// Apply Factor
			event.setDamage(modifier, factor * event.getDamage(modifier));
		}
	}
	
	// -------------------------------------------- //
	// GET IP
	// -------------------------------------------- //
	
	public static String getIp(CommandSender sender)
	{
		if (!(sender instanceof Player)) return null;
		Player player = (Player)sender;
		
		InetSocketAddress address = player.getAddress();
		if (address == null) return null;
		
		String ret = address.toString();
		String[] parts = ret.split("/");
        
        ret = parts[1];
        parts = ret.split(":");
        
        ret = parts[0];
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
	
	public static <T> T recurseResolveMap(T input, Map<T, T> map)
	{
		T output = map.get(input);
		if (output == null) return input;
		return recurseResolveMap(output, map);
	}
	
	// -------------------------------------------- //
	// GET BLOCKS
	// -------------------------------------------- //
	
	public static List<Block> getBlocks(Location location, int halfWidth)
	{
		return getBlocks(location.getBlock(), halfWidth);
	}
	
	public static List<Block> getBlocks(Block block, int halfWidth)
	{
		int xmin = block.getX() - halfWidth;
		int ymin = block.getY() - halfWidth;
		int zmin = block.getZ() - halfWidth;
		
		int xmax = block.getX() + halfWidth;
		int ymax = block.getY() + halfWidth;
		int zmax = block.getZ() + halfWidth;
		
		return getBlocks(block.getWorld(), xmin, ymin, zmin, xmax, ymax, zmax);
	}
	
	public static List<Block> getBlocks(World world, int xmin, int ymin, int zmin, int xmax, int ymax, int zmax)
	{
		List<Block> blocks = new ArrayList<Block>();
		
		for (int x = xmin; x <= xmax; x++)
		{
			for (int y = ymin; y <= ymax; y++)
			{
				for (int z = zmin; z <= zmax; z++)
				{
					blocks.add(world.getBlockAt(x, y, z));
				}	
			}
		}
		
		return blocks;
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
	
	// FOOD
	
	public final static Set<Material> FOOD_MATERIALS = new HashSet<Material>(MUtil.list(
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
	
	// Archery
	
	public static boolean isArchery(Entity entity)
	{
		if (entity == null) return false;
		return entity instanceof Arrow;
	}
	
	public static boolean isArchery(EntityDamageByEntityEvent event)
	{
		return isArchery(event.getDamager());
	}
	
	public static boolean isArchery(EntityDamageEvent event)
	{
		if ( ! (event instanceof EntityDamageByEntityEvent)) return false;
		return isArchery((EntityDamageByEntityEvent)event);
	}
	
	// Sword
	
	public static Set<Material> SWORD_MATERIALS = EnumSet.of(
		Material.WOOD_SWORD,
		Material.STONE_SWORD,
		Material.IRON_SWORD,
		Material.GOLD_SWORD,
		Material.DIAMOND_SWORD
	);
	
	public static boolean isSword(Material material)
	{
		return SWORD_MATERIALS.contains(material);
	}
	
	public static boolean isSword(ItemStack item)
	{
		if (item == null) return false;
		return isSword(item.getType());
	}
	
	public static boolean isSword(Entity entity)
	{
		if (entity == null) return false;
		if (!(entity instanceof LivingEntity)) return false;
		LivingEntity lentity = (LivingEntity)entity;
		return isSword(lentity.getEquipment().getItemInHand());
	}
	
	public static boolean isSword(EntityDamageByEntityEvent event)
	{
		return isSword(event.getDamager());
	}
	
	public static boolean isSword(EntityDamageEvent event)
	{
		if ( ! (event instanceof EntityDamageByEntityEvent)) return false;
		return isSword((EntityDamageByEntityEvent)event);
	}
	
	// Axe
	
	public static Set<Material> AXE_MATERIALS = EnumSet.of(
		Material.WOOD_AXE,
		Material.STONE_AXE,
		Material.IRON_AXE,
		Material.GOLD_AXE,
		Material.DIAMOND_AXE
	);
	
	public static boolean isAxe(Material material)
	{
		return AXE_MATERIALS.contains(material);
	}
	
	public static boolean isAxe(ItemStack item)
	{
		if (item == null) return false;
		return isAxe(item.getType());
	}
	
	public static boolean isAxe(Entity entity)
	{
		if (entity == null) return false;
		if (!(entity instanceof LivingEntity)) return false;
		LivingEntity lentity = (LivingEntity)entity;
		return isAxe(lentity.getEquipment().getItemInHand());
	}
	
	public static boolean isAxe(EntityDamageByEntityEvent event)
	{
		return isAxe(event.getDamager());
	}
	
	public static boolean isAxe(EntityDamageEvent event)
	{
		if ( ! (event instanceof EntityDamageByEntityEvent)) return false;
		return isAxe((EntityDamageByEntityEvent)event);
	}
	
	// Unarmed
	
	public static boolean isUnarmed(ItemStack item)
	{
		if (item == null) return true;
		return InventoryUtil.isNothing(item);
	}
	
	public static boolean isUnarmed(Entity entity)
	{
		if (entity == null) return false;
		if (!(entity instanceof LivingEntity)) return false;
		LivingEntity lentity = (LivingEntity)entity;
		return isUnarmed(lentity.getEquipment().getItemInHand());
	}
	
	public static boolean isUnarmed(EntityDamageByEntityEvent event)
	{
		return isUnarmed(event.getDamager());
	}
	
	public static boolean isUnarmed(EntityDamageEvent event)
	{
		if ( ! (event instanceof EntityDamageByEntityEvent)) return false;
		return isUnarmed((EntityDamageByEntityEvent)event);
	}
	
	public static boolean isAxe(BlockBreakEvent event)
	{
		return isAxe(event.getPlayer().getItemInHand());
	}
	
	// Pickaxe
	
	public static Set<Material> PICKAXE_MATERIALS = EnumSet.of(
		Material.WOOD_PICKAXE,
		Material.STONE_PICKAXE,
		Material.IRON_PICKAXE,
		Material.GOLD_PICKAXE,
		Material.DIAMOND_PICKAXE
	);
		
	public static boolean isPickaxe(Material material)
	{
		return PICKAXE_MATERIALS.contains(material);
	}
	
	public static boolean isPickaxe(ItemStack item)
	{
		if (item == null) return false;
		return isPickaxe(item.getType());
	}
		
	public static boolean isPickaxe(Entity entity)
	{
		if (entity == null) return false;
		if (!(entity instanceof LivingEntity)) return false;
		LivingEntity lentity = (LivingEntity)entity;
		return isPickaxe(lentity.getEquipment().getItemInHand());
	}
		
	public static boolean isPickaxe(BlockBreakEvent event)
	{
		return isPickaxe(event.getPlayer().getItemInHand());
	}
	
	// Spade
	
	public static Set<Material> SPADE_MATERIALS = EnumSet.of(
		Material.WOOD_SPADE,
		Material.STONE_SPADE,
		Material.IRON_SPADE,
		Material.GOLD_SPADE,
		Material.DIAMOND_SPADE
	);
			
	public static boolean isSpade(Material material)
	{
		return SPADE_MATERIALS.contains(material);
	}
		
	public static boolean isSpade(ItemStack item)
	{
		if (item == null) return false;
		return isSpade(item.getType());
	}
			
	public static boolean isSpade(Entity entity)
	{
		if (entity == null) return false;
		if (!(entity instanceof LivingEntity)) return false;
		LivingEntity lentity = (LivingEntity)entity;
		return isSpade(lentity.getEquipment().getItemInHand());
	}
			
	public static boolean isSpade(BlockBreakEvent event)
	{
		return isSpade(event.getPlayer().getItemInHand());
	}
		
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
		else if (FOOD_MATERIALS.contains(event.getMaterial()))
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
		return MassiveCoreEngineMain.kickedPlayerReasons.get(event.getPlayer().getName());
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
		return MassiveCoreEngineWorldNameSet.get().getWorldNames();
	}
	
	// -------------------------------------------- //
	// SIMPLE CONSTRUCTORS
	// -------------------------------------------- //
	
	@SafeVarargs
	public static <T> List<T> list(T... items)
	{
		return new MassiveList<T>(Arrays.asList(items));
	}
	
	@SafeVarargs
	public static <T> Set<T> set(T... items)
	{
		return new MassiveSet<T>(Arrays.asList(items));
	}
	
	public static Set<String> treeset(String... items)
	{
		return new MassiveTreeSet<String, CaseInsensitiveComparator>(CaseInsensitiveComparator.get(), Arrays.asList(items));
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
	
	public static <K, V> Map<V, Set<K>> reverseIndex(Map<K, V> map)
	{
		Map<V, Set<K>> ret = new LinkedHashMap<V, Set<K>>();
		
		for (Entry<K, V> entry : map.entrySet())
		{
			K key = entry.getKey();
			V value = entry.getValue();
			
			Set<K> set = ret.get(value);
			if (set == null)
			{
				set = new HashSet<K>();
				ret.put(value, set);
			}
			set.add(key);
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
		int index = MassiveCore.random.nextInt(coll.size());
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
			int index = MassiveCore.random.nextInt(ret.size());
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
		Integer ret = compareNulls(herp, derp);
		if (ret != null) return ret;
		return herp.compareTo(derp);
	}
	
	public static Integer compareNulls(Object one, Object two)
	{
		if (one == null && two == null) return 0;
		if (one == null) return -1;
		if (two == null) return +1;
		return null;
	}
	
	public static Integer compareWithList(Object one, Object two, List<? extends Object> list)
	{
		int oneIndex = list.indexOf(one);
		int twoIndex = list.indexOf(two);
		if (oneIndex != -1 && twoIndex != -1) return oneIndex - twoIndex;
		if (oneIndex != -1) return -1;
		if (twoIndex != -1) return +1;
		return null;
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
		if (MassiveCore.random.nextDouble() < prob) ret += 1;
		return ret;
	}
	
	public static double round(double value, int places)
	{
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
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
		
		// The accountId extractor is used for the money mixin.
		// For now we act on the name instead of the ID since vault needs names.
		registerExtractor(String.class, "accountId", ExtractorSenderName.get());
	}
	
}
