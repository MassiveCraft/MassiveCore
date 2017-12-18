package com.massivecraft.massivecore.item;

import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.collections.MassiveListDef;
import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.collections.MassiveTreeMapDef;
import com.massivecraft.massivecore.collections.MassiveTreeSetDef;
import com.massivecraft.massivecore.command.editor.annotation.EditorMethods;
import com.massivecraft.massivecore.command.editor.annotation.EditorType;
import com.massivecraft.massivecore.command.editor.annotation.EditorTypeInner;
import com.massivecraft.massivecore.command.editor.annotation.EditorVisible;
import com.massivecraft.massivecore.command.type.TypeMaterialId;
import com.massivecraft.massivecore.command.type.convert.TypeConverterColor;
import com.massivecraft.massivecore.command.type.convert.TypeConverterDyeColor;
import com.massivecraft.massivecore.command.type.convert.TypeConverterEnchant;
import com.massivecraft.massivecore.command.type.convert.TypeConverterItemFlag;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.command.type.primitive.TypeStringParsed;
import com.massivecraft.massivecore.comparator.ComparatorSmart;
import com.massivecraft.massivecore.util.InventoryUtil;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.xlib.gson.annotations.SerializedName;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

/**
 * This class makes use of primitives, collections and maps only.
 * All Bukkit specific enumerations and classes are avoided.
 * That means this class itself is compatible with all Bukkit server versions.
 * 
 * We also make sure to only initialize variables with null as value.
 * Null means "default" and this way we save database space as well as CPU power on class construction.
 * 
 * This class acts as a safe intermediary for database storage.
 * It is mainly used by the ItemStackAdapter and InventoryAdapter.
 * It can also be used directly, for example in maps, since it provides a stable equals and hash code method (as opposed to Bukkit). 
 */
@EditorMethods(true)
public class DataItemStack implements Comparable<DataItemStack>
{
	// -------------------------------------------- //
	// DEFAULTS
	// -------------------------------------------- //
	// The default values are used in the logic of both getters and setters.
	// For that reason they are immutable.
	//
	// We avoid null in all locations except where Bukkit makes use of null.
	// Since Bukkit doesn't NPE evade much we save ourselves a lot of trouble that way.
	// Especially note how all collections default to empty immutables instead of null.
	
	public static final transient String DEFAULT_ID = Material.AIR.name();
	public static final transient Integer DEFAULT_COUNT = 1;
	public static final transient Integer DEFAULT_DAMAGE = 0;
	public static final transient String DEFAULT_NAME = null;
	public static final transient List<String> DEFAULT_LORE = Collections.emptyList();
	public static final transient Map<Integer, Integer> DEFAULT_ENCHANTS = Collections.emptyMap();
	public static final transient Integer DEFAULT_REPAIRCOST = 0;
	public static final transient String DEFAULT_TITLE = null;
	public static final transient String DEFAULT_AUTHOR = null;
	public static final transient List<String> DEFAULT_PAGES = Collections.emptyList();
	public static final transient Integer DEFAULT_COLOR = Bukkit.getItemFactory().getDefaultLeatherColor().asRGB();
	public static final transient Boolean DEFAULT_SCALING = false;
	public static final transient List<DataPotionEffect> DEFAULT_POTION_EFFECTS = Collections.emptyList();
	public static final transient String DEFAULT_SKULL = null;
	public static final transient DataFireworkEffect DEFAULT_FIREWORK_EFFECT = null;
	public static final transient List<DataFireworkEffect> DEFAULT_FIREWORK_EFFECTS = Collections.emptyList();
	public static final transient Integer DEFAULT_FIREWORK_FLIGHT = 0;
	public static final transient Map<Integer, Integer> DEFAULT_STORED_ENCHANTS = Collections.emptyMap();
	public static final transient Boolean DEFAULT_UNBREAKABLE = false;
	public static final transient Set<String> DEFAULT_FLAGS = Collections.emptySet();
	public static final transient Integer DEFAULT_BANNER_BASE = null;
	public static final transient List<DataBannerPattern> DEFAULT_BANNER_PATTERNS = Collections.emptyList();
	public static final transient String DEFAULT_POTION = "water";
	public static final transient Map<Integer, DataItemStack> DEFAULT_INVENTORY = Collections.emptyMap();
	public static final transient Integer DEFAULT_POTION_COLOR = null;
	public static final transient Integer DEFAULT_MAP_COLOR = null;
	
	// -------------------------------------------- //
	// FIELDS > BASIC
	// -------------------------------------------- //
	
	@EditorType(value = TypeMaterialId.class)
	private String id = null;
	public String getId() { return get(this.id, DEFAULT_ID); }
	public DataItemStack setId(String id) { this.id = set(id, DEFAULT_ID); return this; }
	
	private Integer count = null;
	public int getCount() { return get(this.count, DEFAULT_COUNT); }
	public DataItemStack setCount(int count) { this.count = set(count, DEFAULT_COUNT); return this; }
	
	private Integer damage = null;
	public int getDamage() { return get(this.damage, DEFAULT_DAMAGE); }
	public DataItemStack setDamage(int damage) { this.damage = set(damage, DEFAULT_DAMAGE); return this; }
	
	// -------------------------------------------- //
	// FIELDS > UNSPECIFIC
	// -------------------------------------------- //
	
	@EditorType(TypeStringParsed.class)
	private String name = null;
	public String getName() { return get(this.name, DEFAULT_NAME); }
	public DataItemStack setName(String name) { this.name = set(name, DEFAULT_NAME); return this; }
	
	@EditorTypeInner(TypeStringParsed.class)
	private MassiveListDef<String> lore = null;
	public List<String> getLore() { return get(this.lore, DEFAULT_LORE); }
	public DataItemStack setLore(List<String> lore) { this.lore = set(lore, DEFAULT_LORE); return this; }
	
	// The Bukkit ItemMeta#getEnchants() is not sorted by the enchant id.
	// There may be some sort of custom sorting order, I'm not sure.
	// We are however enforcing sorting by the enchant id ourselves to be sure.
	@EditorTypeInner({TypeConverterEnchant.class, TypeInteger.class})
	private MassiveTreeMapDef<Integer, Integer, ComparatorSmart> enchants = null;
	public Map<Integer, Integer> getEnchants() { return get(this.enchants, DEFAULT_ENCHANTS); }
	public DataItemStack setEnchants(Map<Integer, Integer> enchants) { this.enchants = set(enchants, DEFAULT_ENCHANTS); return this; }
	
	private Integer repaircost = null;
	public int getRepaircost() { return get(this.repaircost, DEFAULT_REPAIRCOST); }
	public DataItemStack setRepaircost(int repaircost) { this.repaircost = set(repaircost, DEFAULT_REPAIRCOST); return this; }
	
	// -------------------------------------------- //
	// FIELDS > BOOK
	// -------------------------------------------- //
	
	@EditorType(TypeStringParsed.class)
	private String title = null;
	public String getTitle() { return get(this.title, DEFAULT_TITLE); }
	public DataItemStack setTitle(String title) { this.title = set(title, DEFAULT_TITLE); return this; }
	
	private String author = null;
	public String getAuthor() { return get(this.author, DEFAULT_AUTHOR); }
	public DataItemStack setAuthor(String author) { this.author = set(author, DEFAULT_AUTHOR); return this; }
	
	@EditorTypeInner(TypeStringParsed.class)
	private MassiveListDef<String> pages = null;
	public List<String> getPages() { return get(this.pages, DEFAULT_PAGES); }
	public DataItemStack setPages(Collection<String> pages) { this.pages = set(pages, DEFAULT_PAGES); return this; }
	
	// -------------------------------------------- //
	// FIELDS > LEATHER ARMOR
	// -------------------------------------------- //
	
	@EditorType(TypeConverterColor.class)
	private Integer color = null;
	public Integer getColor() { return get(this.color, DEFAULT_COLOR); }
	public DataItemStack setColor(int color) { this.color = set(color, DEFAULT_COLOR); return this; }
	
	// -------------------------------------------- //
	// FIELDS > MAP
	// -------------------------------------------- //
	
	private Boolean scaling = null;
	public boolean isScaling() { return get(this.scaling, DEFAULT_SCALING); }
	public DataItemStack setScaling(boolean scaling) { this.scaling = set(scaling, DEFAULT_SCALING); return this; }
	
	// -------------------------------------------- //
	// FIELDS > POTION EFFECTS
	// -------------------------------------------- //
	
	// TODO: Sorting?
	@SerializedName("potion-effects")
	private MassiveListDef<DataPotionEffect> potionEffects = null;
	public List<DataPotionEffect> getPotionEffects() { return get(this.potionEffects, DEFAULT_POTION_EFFECTS); }
	public DataItemStack setPotionEffects(List<DataPotionEffect> potionEffects) { this.potionEffects = set(potionEffects, DEFAULT_POTION_EFFECTS); return this; }
	
	// -------------------------------------------- //
	// FIELDS > SKULL
	// -------------------------------------------- //
	
	private String skull = null;
	public String getSkull() { return get(this.skull, DEFAULT_SKULL); }
	public DataItemStack setSkull(String skull) { this.skull = set(skull, DEFAULT_SKULL); return this; }
	
	// -------------------------------------------- //
	// FIELDS > FIREWORK EFFECT
	// -------------------------------------------- //
	
	@SerializedName("firework-effect")
	private DataFireworkEffect fireworkEffect = null;
	public DataFireworkEffect getFireworkEffect() { return get(this.fireworkEffect, DEFAULT_FIREWORK_EFFECT); }
	public DataItemStack setFireworkEffect(DataFireworkEffect fireworkEffect) { this.fireworkEffect = set(fireworkEffect, DEFAULT_FIREWORK_EFFECT); return this; }
	
	// -------------------------------------------- //
	// FIELDS > FIREWORK
	// -------------------------------------------- //
	
	// TODO: Sorting?
	@SerializedName("firework-effects")
	private MassiveListDef<DataFireworkEffect> fireworkEffects = null;
	public List<DataFireworkEffect> getFireworkEffects() { return get(this.fireworkEffects, DEFAULT_FIREWORK_EFFECTS); }
	public DataItemStack setFireworkEffects(List<DataFireworkEffect> fireworkEffects) { this.fireworkEffects = set(fireworkEffects, DEFAULT_FIREWORK_EFFECTS); return this; }
	
	// NOTE: Did not have a default specified.
	@SerializedName("firework-flight")
	private Integer fireworkFlight = null;
	public int getFireworkFlight() { return get(this.fireworkFlight, DEFAULT_FIREWORK_FLIGHT); }
	public DataItemStack setFireworkFlight(int fireworkFlight) { this.fireworkFlight = set(fireworkFlight, DEFAULT_FIREWORK_FLIGHT); return this; }
	
	// -------------------------------------------- //
	// FIELDS > STORED ENCHANTS
	// -------------------------------------------- //
	
	@EditorTypeInner({TypeConverterEnchant.class, TypeInteger.class})
	@SerializedName("stored-enchants")
	private MassiveTreeMapDef<Integer, Integer, ComparatorSmart> storedEnchants = null;
	public Map<Integer, Integer> getStoredEnchants() { return get(this.storedEnchants, DEFAULT_STORED_ENCHANTS); }
	public DataItemStack setStoredEnchants(Map<Integer, Integer> storedEnchants) { this.storedEnchants = set(storedEnchants, DEFAULT_STORED_ENCHANTS); return this; }
	
	// -------------------------------------------- //
	// FIELDS > UNBREAKABLE
	// -------------------------------------------- //
	// SINCE: 1.8
	
	private Boolean unbreakable = null;
	public boolean isUnbreakable() { return get(this.unbreakable, DEFAULT_UNBREAKABLE); }
	public DataItemStack setUnbreakable(boolean unbreakable) { this.unbreakable = set(unbreakable, DEFAULT_UNBREAKABLE); return this; }
	
	// -------------------------------------------- //
	// FIELDS > FLAGS
	// -------------------------------------------- //
	// SINCE: 1.8
	
	@EditorTypeInner(TypeConverterItemFlag.class)
	private MassiveTreeSetDef<String, ComparatorSmart> flags = null;
	public Set<String> getFlags() { return get(this.flags, DEFAULT_FLAGS); }
	public DataItemStack setFlags(Set<String> flags) { this.flags = set(flags, DEFAULT_FLAGS); return this; }
	
	// -------------------------------------------- //
	// FIELDS > BANNER BASE
	// -------------------------------------------- //
	// SINCE: 1.8
	// The integer is the dye color byte representation.
	// Is actually nullable in Bukkit.
	
	@EditorType(TypeConverterDyeColor.class)
	@SerializedName("banner-base")
	private Integer bannerBase = null;
	public Integer getBannerBase() { return get(this.bannerBase, DEFAULT_BANNER_BASE); }
	public DataItemStack setBannerBase(Integer bannerBase) { this.bannerBase = set(bannerBase, DEFAULT_BANNER_BASE); return this; }
	
	// -------------------------------------------- //
	// FIELDS > BANNER PATTERNS
	// -------------------------------------------- //
	// SINCE: 1.8
	// This should really be a list and not a set.
	// The order matters and is explicitly assigned.
	
	@SerializedName("banner")
	private MassiveListDef<DataBannerPattern> bannerPatterns = null;
	public List<DataBannerPattern> getBannerPatterns() { return get(this.bannerPatterns, DEFAULT_BANNER_PATTERNS); }
	public DataItemStack setBannerPatterns(List<DataBannerPattern> bannerPatterns) { this.bannerPatterns = set(bannerPatterns, DEFAULT_BANNER_PATTERNS); return this;}
	
	// -------------------------------------------- //
	// FIELDS > POTION
	// -------------------------------------------- //
	// SINCE: 1.9
	
	private String potion = null;
	public String getPotion() { return get(this.potion, DEFAULT_POTION); }
	public DataItemStack setPotion(String potion) { this.potion = set(potion, DEFAULT_POTION); return this; }
	
	// -------------------------------------------- //
	// FIELDS > INVENTORY
	// -------------------------------------------- //
	// SINCE: 1.8
	
	@EditorVisible(false)
	private Map<Integer, DataItemStack> inventory = null;
	public Map<Integer, DataItemStack> getInventory() { return get(this.inventory, DEFAULT_INVENTORY); }
	public DataItemStack setInventory(Map<Integer, DataItemStack> inventory) { this.inventory = set(inventory, DEFAULT_INVENTORY); return this; }
	
	// -------------------------------------------- //
	// FIELDS > POTION COLOR
	// -------------------------------------------- //
	// SINCE: 1.11
	
	@EditorType(TypeConverterColor.class)
	private Integer potionColor = null;
	public Integer getPotionColor() { return get(this.potionColor, DEFAULT_POTION_COLOR); }
	public DataItemStack setPotionColor(Integer potionColor) { this.potionColor = set(potionColor, DEFAULT_POTION_COLOR); return this; }
	
	// -------------------------------------------- //
	// FIELDS > MAP COLOR
	// -------------------------------------------- //
	// Since 1.11
	
	@EditorType(TypeConverterColor.class)
	private Integer mapColor = null;
	public Integer getMapColor() { return get(this.mapColor, DEFAULT_MAP_COLOR); }
	public DataItemStack setMapColor(Integer mapColor) { this.mapColor = set(mapColor, DEFAULT_MAP_COLOR); return this; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public DataItemStack()
	{
		
	}
	
	public DataItemStack(ItemStack itemStack)
	{
		this.write(itemStack, false);
	}
	
	// -------------------------------------------- //
	// WRITE
	// -------------------------------------------- //
	
	public void write(ItemStack itemStack, boolean a2b)
	{
		WriterItemStack.get().write(this, itemStack, a2b);
	}
	
	// -------------------------------------------- //
	// CONVERT ONE
	// -------------------------------------------- //
	
	public static DataItemStack fromBukkit(ItemStack itemStack)
	{
		if (itemStack == null) return null;
		return new DataItemStack(itemStack);
	}
	
	public ItemStack toBukkit()
	{
		// Create
		ItemStack ret = WriterItemStack.get().createOB();
		
		// Fill
		this.write(ret, true);
		
		// Return
		return ret;
	}
	
	public static ItemStack toBukkit(DataItemStack dataItemStack)
	{
		if (dataItemStack == null) return null;
		return dataItemStack.toBukkit();
	}
	
	// -------------------------------------------- //
	// CONVERT MANY
	// -------------------------------------------- //
	
	public static void fromBukkit(Iterable<ItemStack> itemStacks, Collection<DataItemStack> dataItemStacks)
	{
		for (ItemStack itemStack : itemStacks)
		{
			dataItemStacks.add(fromBukkit(itemStack));
		}
	}
	
	public static List<DataItemStack> fromBukkit(Iterable<ItemStack> itemStacks)
	{
		// Create
		List<DataItemStack> ret = new MassiveList<>();
		
		// Fill
		fromBukkit(itemStacks, ret);
		
		// Return
		return ret;
	}
	
	public static <V> void fromBukkitKeys(Map<ItemStack, V> itemStacks, Map<DataItemStack, V> dataItemStacks)
	{
		for (Entry<ItemStack, V> entry : itemStacks.entrySet())
		{
			dataItemStacks.put(fromBukkit(entry.getKey()), entry.getValue());
		}
	}
	
	public static <V> Map<DataItemStack, V> fromBukkitKeys(Map<ItemStack, V> itemStacks)
	{
		// Create
		Map<DataItemStack, V> ret = new MassiveMap<>();
		
		// Fill
		fromBukkitKeys(itemStacks, ret);
		
		// Return
		return ret;
	}
	
	public static <K> void fromBukkitValues(Map<K, ItemStack> itemStacks, Map<K, DataItemStack> dataItemStacks)
	{
		for (Entry<K, ItemStack> entry : itemStacks.entrySet())
		{
			dataItemStacks.put(entry.getKey(), fromBukkit(entry.getValue()));
		}
	}
	
	public static <K> Map<K, DataItemStack> fromBukkitValues(Map<K, ItemStack> itemStacks)
	{
		// Create
		Map<K, DataItemStack> ret = new MassiveMap<>();
		
		// Fill
		fromBukkitValues(itemStacks, ret);
		
		// Return
		return ret;
	}
	
	public static Map<Integer, DataItemStack> fromBukkitContents(ItemStack[] contents)
	{
		// Catch NullEmpty
		if (contents == null || contents.length == 0) return null;
		
		// Create
		Map<Integer, DataItemStack> ret = new MassiveMap<>();
		
		// Fill
		for (int i = 0; i < contents.length; i++)
		{
			ItemStack itemStack = contents[i];
			if (InventoryUtil.isNothing(itemStack)) continue;
			
			ret.put(i, DataItemStack.fromBukkit(itemStack));
		}
		
		// Return
		return ret;
	}
	
	public static ItemStack[] toBukkitContents(Map<Integer, DataItemStack> contents)
	{
		// Catch NullEmpty
		if (contents == null || contents.isEmpty()) return null;
		
		// Create
		int max = Collections.max(contents.keySet());
		ItemStack[] ret = new ItemStack[max+1];
		
		// Fill
		for (Entry<Integer, DataItemStack> entry: contents.entrySet())
		{
			int index = entry.getKey();
			DataItemStack item = entry.getValue();
			ret[index] = item.toBukkit();
		}
		
		// Return
		return ret;
	}
	
	// -------------------------------------------- //
	// UTILITY
	// -------------------------------------------- //
	
	public static boolean isSomething(DataItemStack dataItemStack)
	{
		if (dataItemStack == null) return false;
		if (dataItemStack.getId().equals(DEFAULT_ID)) return false;
		// In Minecraft 1.9 zero quantity is a thing.
		return true;
	}
	
	public static boolean isNothing(DataItemStack dataItemStack)
	{
		return ! isSomething(dataItemStack);
	}
	
	// -------------------------------------------- //
	// COMPARE & EQUALS & HASHCODE 
	// -------------------------------------------- //
	
	@Override
	public int compareTo(DataItemStack that)
	{
		return ComparatorSmart.get().compare(
			this.getId(), that.getId(),
			this.getCount(), that.getCount(),
			this.getDamage(), that.getDamage(),
			this.getName(), that.getName(),
			this.getLore(), that.getLore(),
			this.getEnchants(), that.getEnchants(),
			this.getRepaircost(), that.getRepaircost(),
			this.getTitle(), that.getTitle(),
			this.getAuthor(), that.getAuthor(),
			this.getPages(), that.getPages(),
			this.getColor(), that.getColor(),
			this.isScaling(), that.isScaling(),
			this.getPotionEffects(), that.getPotionEffects(),
			this.getSkull(), that.getSkull(),
			this.getFireworkEffect(), that.getFireworkEffect(),
			this.getFireworkEffects(), that.getFireworkEffects(),
			this.getFireworkFlight(), that.getFireworkFlight(),
			this.getStoredEnchants(), that.getStoredEnchants(),
			this.isUnbreakable(), that.isUnbreakable(),
			this.getFlags(), that.getFlags(),
			this.getBannerBase(), that.getBannerBase(),
			this.getBannerPatterns(), that.getBannerPatterns(),
			this.getPotion(), that.getPotion(),
			this.getInventory(), that.getInventory(),
			this.getPotionColor(), that.getPotionColor(),
			this.getMapColor(), that.getMapColor()
		);
	}
	
	@Override
	public boolean equals(Object object)
	{
		if ( ! (object instanceof DataItemStack)) return false;
		DataItemStack that = (DataItemStack)object;
		return MUtil.equals(
			this.getId(), that.getId(),
			this.getCount(), that.getCount(),
			this.getDamage(), that.getDamage(),
			this.getName(), that.getName(),
			this.getLore(), that.getLore(),
			this.getEnchants(), that.getEnchants(),
			this.getRepaircost(), that.getRepaircost(),
			this.getTitle(), that.getTitle(),
			this.getAuthor(), that.getAuthor(),
			this.getPages(), that.getPages(),
			this.getColor(), that.getColor(),
			this.isScaling(), that.isScaling(),
			this.getPotionEffects(), that.getPotionEffects(),
			this.getSkull(), that.getSkull(),
			this.getFireworkEffect(), that.getFireworkEffect(),
			this.getFireworkEffects(), that.getFireworkEffects(),
			this.getFireworkFlight(), that.getFireworkFlight(),
			this.getStoredEnchants(), that.getStoredEnchants(),
			this.isUnbreakable(), that.isUnbreakable(),
			this.getFlags(), that.getFlags(),
			this.getBannerBase(), that.getBannerBase(),
			this.getBannerPatterns(), that.getBannerPatterns(),
			this.getPotion(), that.getPotion(),
			this.getInventory(), that.getInventory(),
			this.getPotionColor(), that.getPotionColor(),
			this.getMapColor(), that.getMapColor()
		);
	}
	
	public boolean equalsItem(ItemStack item)
	{
		if (item == null) return false;
		DataItemStack that = DataItemStack.fromBukkit(item);
		return this.equals(that);
	}
	
	public boolean isSimilar(DataItemStack that)
	{
		// A copy of the equals logic above. However we comment out:
		// * Count
		// * Repaircost
		return MUtil.equals(
			this.getId(), that.getId(),
			// this.getCount(), that.getCount(),
			this.getDamage(), that.getDamage(),
			this.getName(), that.getName(),
			this.getLore(), that.getLore(),
			this.getEnchants(), that.getEnchants(),
			// this.getRepaircost(), that.getRepaircost(),
			this.getTitle(), that.getTitle(),
			this.getAuthor(), that.getAuthor(),
			this.getPages(), that.getPages(),
			this.getColor(), that.getColor(),
			this.isScaling(), that.isScaling(),
			this.getPotionEffects(), that.getPotionEffects(),
			this.getSkull(), that.getSkull(),
			this.getFireworkEffect(), that.getFireworkEffect(),
			this.getFireworkEffects(), that.getFireworkEffects(),
			this.getFireworkFlight(), that.getFireworkFlight(),
			this.getStoredEnchants(), that.getStoredEnchants(),
			this.isUnbreakable(), that.isUnbreakable(),
			this.getFlags(), that.getFlags(),
			this.getBannerBase(), that.getBannerBase(),
			this.getBannerPatterns(), that.getBannerPatterns(),
			this.getPotion(), that.getPotion(),
			this.getInventory(), that.getInventory(),
			this.getPotionColor(), that.getPotionColor(),
			this.getMapColor(), that.getMapColor()
		);
	}
	
	public boolean isSimilarItem(ItemStack item)
	{
		if (item == null) return false;
		DataItemStack that = DataItemStack.fromBukkit(item);
		return this.isSimilar(that);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.getId(),
			this.getCount(),
			this.getDamage(),
			this.getName(),
			this.getLore(),
			this.getEnchants(),
			this.getRepaircost(),
			this.getTitle(),
			this.getAuthor(),
			this.getPages(),
			this.getColor(),
			this.isScaling(),
			this.getPotionEffects(),
			this.getSkull(),
			this.getFireworkEffect(),
			this.getFireworkEffects(),
			this.getFireworkFlight(),
			this.getStoredEnchants(),
			this.isUnbreakable(),
			this.getFlags(),
			this.getBannerBase(),
			this.getBannerPatterns(),
			this.getPotion(),
			this.getInventory(),
			this.getPotionColor(),
			this.getMapColor()
		);
	}
	
	// -------------------------------------------- //
	// GET & SET & NOTHING
	// -------------------------------------------- //
	
	// We treat null and empty collections the same.
	public static boolean isNothing(Object object)
	{
		if (object == null) return true;
		if (object instanceof Collection<?>) return ((Collection<?>)object).isEmpty();
		if (object instanceof Map<?, ?>) return ((Map<?, ?>)object).isEmpty();
		return false;
	}
	
	// Return the value unless the value is nothing then return standard instead.
	public static <T> T get(T value, T standard)
	{
		if (isNothing(value)) return standard;
		return value;
	}
	
	// Return the value unless the value is nothing or standard then return null instead.
	// Perform shallow copy on supported collections.
	@SuppressWarnings("unchecked")
	public static <R, T> R set(T value, T standard)
	{
		if (isNothing(value)) return null;
		if (value.equals(standard)) return null;
		
		if (value instanceof List<?>)
		{
			List<Object> list = (List<Object>)value;
			return (R) new MassiveListDef<>(list);
		}
		else if (value instanceof Set<?>)
		{
			Set<Object> set = (Set<Object>)value;
			return (R) new MassiveTreeSetDef<>(ComparatorSmart.get(), set);
		}
		else if (value instanceof Map<?, ?>)
		{
			Map<Object, Object> map = (Map<Object, Object>)value;
			return (R) new MassiveTreeMapDef<>(ComparatorSmart.get(), map);
		}
		
		return (R) value;
	}
	
}
