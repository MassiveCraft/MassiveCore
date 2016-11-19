package com.massivecraft.massivecore.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
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
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
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
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.metadata.Metadatable;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.collections.ExceptionSet;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.collections.MassiveTreeSet;
import com.massivecraft.massivecore.comparator.ComparatorCaseInsensitive;
import com.massivecraft.massivecore.engine.EngineMassiveCoreDatabase;
import com.massivecraft.massivecore.engine.EngineMassiveCoreMain;
import com.massivecraft.massivecore.engine.EngineMassiveCoreWorldNameSet;
import com.massivecraft.massivecore.mixin.MixinMessage;
import com.massivecraft.massivecore.nms.NmsEntityGet;
import com.massivecraft.massivecore.predicate.Predicate;
import com.massivecraft.massivecore.predicate.PredicateElementGarbage;
import com.massivecraft.massivecore.predicate.PredicateElementSignificant;
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
	
	public static Collection<Player> getOnlinePlayers()
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
			Collection<Player> playersCollection = (Collection<Player>)playersObject;
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
	// GET NEARBY PLAYERS
	// -------------------------------------------- //
	
	public static Set<Player> getNearbyPlayers(Entity entity, double raidus, boolean includeSelf)
	{
		Set<Player> ret = getNearbyPlayers(entity.getLocation(), raidus);
		if (isPlayer(entity) && !includeSelf) ret.remove(entity);
		return ret;
	}
	
	public static Set<Player> getNearbyPlayers(Location location, double radius)
	{
		// Create
		Set<Player> ret = new MassiveSet<>();
		
		// Fill
		final World world = location.getWorld();
		final double radiusSquared = radius * radius;
		for (Player player : MUtil.getOnlinePlayers())
		{
			Location playerLocation = player.getLocation();
			World playerWorld = playerLocation.getWorld();
			if ( ! world.equals(playerWorld)) continue;
			double distanceSquared = location.distanceSquared(playerLocation);
			if (distanceSquared > radiusSquared) continue;
			ret.add(player);
		}
		
		// Return
		return ret;
	}
	
	// -------------------------------------------- //
	// IS SYNCHRONOUS
	// -------------------------------------------- //
	
	public static boolean isSynchronous()
	{
		return Thread.holdsLock(Bukkit.getPluginManager()) || Bukkit.isPrimaryThread();
	}
	
	// -------------------------------------------- //
	// GET ENTITY
	// -------------------------------------------- //
	
	public static Entity getEntity(World world, UUID uuid)
	{
		return NmsEntityGet.get().getEntity(world, uuid);
	}
	
	public static Entity getEntity(UUID uuid)
	{
		return NmsEntityGet.get().getEntity(uuid);
	}
	
	// -------------------------------------------- //
	// IS VALID PLAYER NAME
	// -------------------------------------------- //
	
	// The regex for a valid minecraft player name.
	public final static Pattern PATTERN_PLAYER_NAME = Pattern.compile("^[a-zA-Z0-9_]{2,16}$");
	
	public static boolean isValidPlayerName(String string)
	{
		return PATTERN_PLAYER_NAME.matcher(string).matches();
	}
	
	// -------------------------------------------- //
	// UUID
	// -------------------------------------------- //
	
	public static UUID asUuid(String string)
	{
		// Null
		if (string == null) return null;
		
		// Avoid Exception
		if (string.length() != 36) return null;
		
		// Try
		try
		{
			return UUID.fromString(string);
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	public static boolean isUuid(String string)
	{
		return asUuid(string) != null;
	}
	
	// -------------------------------------------- //
	// CONTAINS COMMAND
	// -------------------------------------------- //
	
	public static boolean containsCommand(String needle, ExceptionSet haystack)
	{
		boolean ret = haystack.isStandard();
		if (containsCommand(needle, haystack.exceptions)) ret = !ret; 
		return ret;
	}
	
	public static boolean containsCommand(String needle, Iterable<String> haystack)
	{
		if (needle == null) return false;
		needle = prepareCommand(needle);
		
		for (String straw : haystack)
		{
			if (straw == null) continue;
			straw = prepareCommand(straw);
			
			// If it starts with then it is possibly a subject.
			if ( ! needle.startsWith(straw)) continue;
			
			// Get the remainder.
			String remainder = needle.substring(straw.length());
			
			// If they were equal, definitely true.
			if (remainder.isEmpty()) return true;
			
			// If the next is a space, the space is used as separator for sub commands or arguments.
			// Otherwise it might just have been another command coincidentally starting with the first command.
			// The old behaviour was if (needle.startsWith(straw)) return true;
			// If "s" was block, then all commands starting with "s" was, now it isn't.
			if (remainder.startsWith(" ")) return true;
		}
		
		return false;
	}
	
	private static String prepareCommand(String string)
	{
		if (string == null) return null;
		string = Txt.removeLeadingCommandDust(string);
		string = string.toLowerCase();
		string = string.trim();
		return string;
	}
	
	// -------------------------------------------- //
	// IP
	// -------------------------------------------- //
	
	public static String getIp(CommandSender sender)
	{
		if (!(sender instanceof Player)) return null;
		Player player = (Player)sender;
		
		InetSocketAddress address = player.getAddress();
		if (address != null) return getIp(address);
		
		String id = IdUtil.getId(player);
		PlayerLoginEvent event = EngineMassiveCoreDatabase.idToPlayerLoginEvent.get(id);
		if (event != null) return getIp(event);
		
		return null;
	}
	
	public static String getIp(InetSocketAddress address)
	{
		if (address == null) return null;
		
		String ret = address.toString();
		String[] parts = ret.split("/");
		
		ret = parts[1];
		parts = ret.split(":");
		
		ret = parts[0];
		return ret;
	}
	
	public static String getIp(PlayerLoginEvent event)
	{
		InetAddress address = event.getAddress();
		return getIp(address);
	}
	
	public static String getIp(InetAddress address)
	{
		if (address == null) return null;
		
		String ret = address.toString();
		String[] parts = ret.split("/");
		
		ret = parts[1];
		return ret;
	}
	
	public static Pattern PATTERN_IPV4 = Pattern.compile("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
	public static boolean isIp(String string)
	{
		if (string == null) return false;
		return PATTERN_IPV4.matcher(string).matches();
	}
	
	// -------------------------------------------- //
	// IS(NT) NPC, SENDER, PLAYER
	// -------------------------------------------- //
	
	public static boolean isNpc(Object object)
	{
		if ( ! (object instanceof Metadatable)) return false;
		Metadatable metadatable = (Metadatable)object;
		try
		{
			return metadatable.hasMetadata("NPC");
		}
		catch (UnsupportedOperationException e)
		{
			// ProtocolLib
			// UnsupportedOperationException: The method hasMetadata is not supported for temporary players.
			return false;
		}
		
	}
	public static boolean isntNpc(Object object)
	{
		return !isNpc(object);
	}
	
	public static boolean isSender(Object object)
	{
		if (!(object instanceof CommandSender)) return false;
		if (isNpc(object)) return false;
		return true;
	}
	public static boolean isntSender(Object object)
	{
		return !isSender(object);
	}
	
	public static boolean isPlayer(Object object)
	{
		if (!(object instanceof Player)) return false;
		if (isNpc(object)) return false;
		return true;
	}
	public static boolean isntPlayer(Object object)
	{
		return !isPlayer(object);
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
		return new MassiveList<>(Arrays.asList(elements));
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
	// STACK TRACE: CUT
	// -------------------------------------------- //
	
	public static void cutStackTrace(List<StackTraceElement> trace)
	{
		// Cut Significant
		int index = 0;
		while (index < trace.size())
		{
			StackTraceElement element = trace.get(index);
			if (PredicateElementSignificant.get().apply(element)) break;
			index++;
		}
		trace.subList(index, trace.size()).clear();
		
		// Reverse
		Collections.reverse(trace);
		
		// Eat Garbage
		for (Iterator<StackTraceElement> iterator = trace.iterator(); iterator.hasNext();)
		{
			StackTraceElement element = iterator.next();
			if (PredicateElementGarbage.get().apply(element))
			{
				iterator.remove();
			}
		}
		
		// Unreverse
		Collections.reverse(trace);
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
	public static List<String> getStackTraceStrings(int skip, boolean color)
	{
		skip++;
		return getStackTraceStrings(getStackTrace(skip), color);
	}
	public static List<String> getStackTraceStrings(boolean color)
	{
		return getStackTraceStrings(0, color);
	}
	
	public static String getStackTraceString(List<StackTraceElement> stackTrace, boolean color)
	{
		return Txt.implode(getStackTraceStrings(stackTrace, color), "\n");
	}
	public static String getStackTraceString(int skip, boolean color)
	{
		skip++;
		return getStackTraceString(getStackTrace(skip), color);
	}
	public static String getStackTraceString(boolean color)
	{
		return getStackTraceString(0, color);
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
	// STACK TRACE: DEBUG
	// -------------------------------------------- //
	
	public static void stackTraceDebug(String title)
	{
		// Create
		List<String> messages = new MassiveList<>();
		
		// Fill
		messages.add(Txt.titleize(title).toPlain(true));
		messages.addAll(MUtil.getStackTraceStrings(1, true));
		
		// Message
		MixinMessage.get().messageOne(IdUtil.CONSOLE_ID, messages);
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
	public static List<Integer> range(int from, int to)
	{
		List<Integer> ret = new MassiveList<>(to - from);
		for (int i = from; i < to; i++)
		{
			ret.add(i);
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
	// MAP OPERATIONS
	// -------------------------------------------- //
	
	public static void keepLeft(Map<?, ?> map, int maxSize)
	{
		int i = 0;
		Iterator<?> iter = map.entrySet().iterator();
		while (iter.hasNext())
		{
			iter.next();
			i++;
			if (i > maxSize) iter.remove();
		}
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
	
	public static int getChatColorCode(ChatColor chatColor)
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
	
	public static ChatColor getChatColor(int chatColorCode)
	{
		switch (chatColorCode)
		{
			case 0x00: return ChatColor.BLACK;
			case 0x1: return ChatColor.DARK_BLUE;
			case 0x2: return ChatColor.DARK_GREEN;
			case 0x3: return ChatColor.DARK_AQUA;
			case 0x4: return ChatColor.DARK_RED;
			case 0x5: return ChatColor.DARK_PURPLE;
			case 0x6: return ChatColor.GOLD;
			case 0x7: return ChatColor.GRAY;
			case 0x8: return ChatColor.DARK_GRAY;
			case 0x9: return ChatColor.BLUE;
			case 0xA: return ChatColor.GREEN;
			case 0xB: return ChatColor.AQUA;
			case 0xC: return ChatColor.RED;
			case 0xD: return ChatColor.LIGHT_PURPLE;
			case 0xE: return ChatColor.YELLOW;
			case 0xF: return ChatColor.WHITE;
			case 0x10: return ChatColor.MAGIC;
			case 0x11: return ChatColor.BOLD;
			case 0x12: return ChatColor.STRIKETHROUGH;
			case 0x13: return ChatColor.UNDERLINE;
			case 0x14: return ChatColor.ITALIC;
			case 0x15: return ChatColor.RESET;
		}
		throw new IllegalArgumentException("The chat color code " + chatColorCode + " is not yet supported!");
	}
	
	@SuppressWarnings("deprecation")
	public static ChatColor getChatColor(DyeColor dyeColor)
	{
		int woolColorCode = dyeColor.getWoolData();
		
		switch (woolColorCode)
		{
			case 0x0: return ChatColor.WHITE;
			case 0x1: return ChatColor.GOLD;
			case 0x2: return ChatColor.LIGHT_PURPLE;
			case 0x3: return ChatColor.AQUA;
			case 0x4: return ChatColor.YELLOW;
			case 0x5: return ChatColor.GREEN;
			case 0x6: return ChatColor.LIGHT_PURPLE;
			case 0x7: return ChatColor.DARK_GRAY;
			case 0x8: return ChatColor.GRAY;
			case 0x9: return ChatColor.DARK_AQUA;
			case 0xA: return ChatColor.DARK_PURPLE;
			case 0xB: return ChatColor.BLUE;
			case 0xC: return ChatColor.GRAY;
			case 0xD: return ChatColor.DARK_GREEN;
			case 0xE: return ChatColor.RED;
			case 0xF: return ChatColor.BLACK;
		}
		throw new IllegalArgumentException("The dye color " + dyeColor + " is not yet supported!");
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
	
	public static void setDamage(EntityDamageEvent event, double newDamage)
	{
		// Check New Damage
		if ( ! isFinite(newDamage)) throw new IllegalStateException("not finite newDamage: " + newDamage);
		
		// Get Old Damage 
		final double oldDamage = event.getDamage(DamageModifier.BASE);
		
		// Check Old Damage
		if ( ! isFinite(oldDamage)) throw new IllegalStateException("not finite oldDamage: " + oldDamage);
		
		// No Change?
		if (newDamage == oldDamage) return;
		
		// No Old Damage? (Avoid DBZ)
		if (oldDamage == 0) return;
		
		// Get Factor
		final double factor = newDamage / oldDamage;
		
		// Check Factor
		if ( ! isFinite(factor)) throw new IllegalStateException("not finite factor: " + factor + " damage: " + newDamage + " oldDamage: " + oldDamage);
		
		// Now scale all damage modifiers!
		for (DamageModifier modifier : DamageModifier.values())
		{
			// Is this modifier used in the event?
			if ( ! event.isApplicable(modifier)) continue;
			
			// Is it the base damage? Cool! Just set it directly!
			// We could have used the factor too but then we might suffer floating point degredation.
			if (modifier == DamageModifier.BASE)
			{
				event.setDamage(modifier, newDamage);
				continue;
			}
			
			// Apply Factor
			event.setDamage(modifier, factor * event.getDamage(modifier));
		}
	}
	
	// Same as above but scales directly.
	public static void scaleDamage(EntityDamageEvent event, double factor)
	{
		// Clean Input
		if ( ! isFinite(factor)) throw new IllegalStateException("not finite factor: " + factor);
		
		// No Change?
		if (equalsish(factor, 1)) return;
		
		for (DamageModifier modifier : DamageModifier.values())
		{
			// Is this modifier used in the event?
			if ( ! event.isApplicable(modifier)) continue;
			
			// Apply Factor
			event.setDamage(modifier, factor * event.getDamage(modifier));
		}
	}
	
	// isFinite check recreation to be compatible with java 1.7/1.6
	public static boolean isFinite(double d)
	{
		 return Math.abs(d) <= Double.MAX_VALUE;
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
		return isAxe(InventoryUtil.getWeapon(event.getPlayer()));
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
		return isPickaxe(InventoryUtil.getWeapon(event.getPlayer()));
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
		return isSpade(InventoryUtil.getWeapon(event.getPlayer()));
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
		Player player = event.getPlayer();
		if (MUtil.isntPlayer(player)) return null;
		UUID uuid = player.getUniqueId();
		
		return EngineMassiveCoreMain.kickedPlayerReasons.get(uuid);
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
		return EngineMassiveCoreWorldNameSet.get().getWorldNames();
	}
	
	// -------------------------------------------- //
	// TRANSFORM
	// -------------------------------------------- //
	
	public static <T> List<T> transform(Iterable<T> items, Predicate<? super T> where, Comparator<? super T> orderby, Integer limit, Integer offset)
	{
		// Collection
		Collection<T> collection = null;
		if (items instanceof Collection<?>) collection = (Collection<T>)items;
		
		// WHERE
		List<T> ret;
		if (where == null)
		{
			if (collection != null)
			{
				ret = new ArrayList<T>(collection);
			}
			else
			{
				ret = new ArrayList<T>();
				for (T item : items)
				{
					ret.add(item);
				}
			}
		}
		else
		{
			if (collection != null)
			{
				ret = new ArrayList<T>(collection.size());
			}
			else
			{
				ret = new ArrayList<T>();
			}
			
			for (T item : items)
			{
				if (where.apply(item))
				{
					ret.add(item);
				}
			}
		}
		
		// ORDERBY
		if (orderby != null)
		{
			Collections.sort(ret, orderby);
		}
		
		// LIMIT AND OFFSET
		// Parse args
		int fromIndex = 0;
		if (offset != null)
		{
			fromIndex = offset;
		}
		
		int toIndex = ret.size()-1;
		if (limit != null)
		{
			toIndex = offset+limit;
		}
		
		// Clean args
		if (fromIndex <= 0)
		{
			fromIndex = 0;
		}
		else if (fromIndex > ret.size()-1)
		{
			fromIndex = ret.size()-1;
		}
		
		if (toIndex < fromIndex)
		{
			toIndex = fromIndex;
		}
		else if (toIndex > ret.size()-1)
		{
			toIndex = ret.size()-1;
		}
		
		// No limit?
		if (fromIndex == 0 && toIndex == ret.size()-1) return ret;
		
		return new ArrayList<T>(ret.subList(fromIndex, toIndex));
	}
	public static <T> List<T> transform(Iterable<T> items, Predicate<? super T> where) { return transform(items, where, null, null, null); }
	public static <T> List<T> transform(Iterable<T> items, Predicate<? super T> where, Comparator<? super T> orderby) { return transform(items, where, orderby, null, null); }
	public static <T> List<T> transform(Iterable<T> items, Predicate<? super T> where, Comparator<? super T> orderby, Integer limit) { return transform(items, where, orderby, limit, null); }
	public static <T> List<T> transform(Iterable<T> items, Predicate<? super T> where, Integer limit) { return transform(items, where, null, limit, null); }
	public static <T> List<T> transform(Iterable<T> items, Predicate<? super T> where, Integer limit, Integer offset) { return transform(items, where, null, limit, offset); }
	public static <T> List<T> transform(Iterable<T> items, Comparator<? super T> orderby) { return transform(items, null, orderby, null, null); }
	public static <T> List<T> transform(Iterable<T> items, Comparator<? super T> orderby, Integer limit) { return transform(items, null, orderby, limit, null); }
	public static <T> List<T> transform(Iterable<T> items, Comparator<? super T> orderby, Integer limit, Integer offset) { return transform(items, null, orderby, limit, offset); }
	public static <T> List<T> transform(Iterable<T> items, Integer limit) { return transform(items, null, null, limit, null); }
	public static <T> List<T> transform(Iterable<T> items, Integer limit, Integer offset) { return transform(items, null, null, limit, offset); }
	
	// -------------------------------------------- //
	// SIMPLE CONSTRUCTORS
	// -------------------------------------------- //
	
	@SafeVarargs
	public static <T> List<T> list(T... items)
	{
		List<T> ret = new MassiveList<T>(items.length);
		Collections.addAll(ret, items);
		return ret;
	}
	
	@SafeVarargs
	public static <T> Set<T> set(T... items)
	{
		Set<T> ret = new MassiveSet<T>(items.length);
		Collections.addAll(ret, items);
		return ret;
	}
	
	public static Set<String> treeset(String... items)
	{
		return new MassiveTreeSet<String, ComparatorCaseInsensitive>(ComparatorCaseInsensitive.get(), Arrays.asList(items));
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
		
		List<T> list = null;
		if (coll instanceof List<?>)
		{
			list = (List<T>)coll;
		}
		else
		{
			list = new ArrayList<T>(coll);
		}
		
		int index = MassiveCore.random.nextInt(list.size());
		return list.get(index);
	}
	
	public static <T> List<T> randomSubset(Collection<T> coll, int count)
	{
		// Clean Input
		if (count < 0) count = 0;
		
		// Create Ret
		List<T> ret = new ArrayList<T>(coll);
		while (ret.size() > count)
		{
			int index = MassiveCore.random.nextInt(ret.size());
			ret.remove(index);
		}
		
		// Return Ret
		return ret;
	}
	
	public static <E> List<E> random(List<E> list, int count)
	{
		// Create Ret
		List<E> ret = new MassiveList<E>();
		
		// Empty
		if (list.isEmpty()) return ret;
		
		// Fill Ret
		for (int i = 0; i < count; i++)
		{
			ret.add(MUtil.random(list));
		}
		
		// Return Ret
		return ret;
	}
	
	// -------------------------------------------- //
	// EQUALS
	// -------------------------------------------- //
	
	public static boolean equals(Object object1, Object object2)
	{
		if (object1 == null) return object2 == null;
		if (object2 == null) return false;
		
		return object1.equals(object2);
	}
	
	public static boolean equals(Object... objects)
	{
		if (objects == null) throw new NullPointerException("objects");
		if (objects.length % 2 != 0) throw new IllegalArgumentException("objects length not even");
		
		int index = 1;
		while (index < objects.length)
		{
			Object object1 = objects[index - 1];
			Object object2 = objects[index];
			
			if ( ! equals(object1, object2)) return false;
			
			index += 2;
		}
		
		return true;
	}
	
	// -------------------------------------------- //
	// EQUALSISH
	// -------------------------------------------- //
	
	public static final double EQUALSISH_EPSILON = 0.0001;
	
	public static boolean equalsish(Number number1, Number number2)
	{
		if (number1 == null) return number2 == null;
		if (number2 == null) return false;
		
		return Math.abs(number1.doubleValue() - number2.doubleValue()) < EQUALSISH_EPSILON;
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
		if (d.doubleValue() < min.doubleValue())
		{
			return min;
		}
		
		if (d.doubleValue() > max.doubleValue())
		{
			return max;
		}
		
		return d;
	}
	
	public static long probabilityRound(double value)
	{
		long ret = (long) Math.floor(value);
		double probability = value % 1;
		if (MassiveCore.random.nextDouble() < probability) ret += 1;
		return ret;
	}
	
	public static int probabilityRound(float value)
	{
		int ret = (int) Math.floor(value);
		float probability = value % 1;
		if (MassiveCore.random.nextFloat() < probability) ret += 1;
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
