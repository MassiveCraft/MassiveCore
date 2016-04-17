package com.massivecraft.massivecore;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.block.banner.Pattern;
import org.bukkit.potion.PotionEffect;

import com.massivecraft.massivecore.collections.MassiveListDef;
import com.massivecraft.massivecore.collections.MassiveTreeMapDef;
import com.massivecraft.massivecore.collections.MassiveTreeSetDef;
import com.massivecraft.massivecore.comparator.ComparatorHashCode;
import com.massivecraft.massivecore.xlib.gson.annotations.SerializedName;

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
public class ItemData
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	// Immutable Defaults
	public static final transient int DEFAULT_ID = 0;
	public static final transient int DEFAULT_COUNT = 1;
	public static final transient int DEFAULT_DAMAGE = 0;
	public static final transient boolean DEFAULT_SCALING = false;
	public static final transient boolean DEFAULT_UNBREAKABLE = false;
	
	// -------------------------------------------- //
	// FIELDS > BASIC
	// -------------------------------------------- //
	
	private Integer id = null;
	public int getId() { return (this.id == null ? DEFAULT_ID : this.id); }
	public ItemData setId(int id) { this.id = (id == DEFAULT_ID ? null : id); return this; }
	
	private Integer count = null;
	public int getCount() { return (this.count == null ? DEFAULT_COUNT : this.count); }
	public ItemData setCount(int count) { this.count = (count == DEFAULT_COUNT ? null : count); return this; }
	
	private Integer damage = null;
	public int getDamage() { return (this.damage == null ? DEFAULT_DAMAGE : this.damage); }
	public ItemData setDamage(int damage) { this.damage = (damage == DEFAULT_DAMAGE ? null: damage); return this; }
	
	// -------------------------------------------- //
	// FIELDS > UNSPECIFIC
	// -------------------------------------------- //
	
	private String name = null;
	public String getName() { return this.name; }
	public ItemData setName(String name) { this.name = name; return this; }
	
	private MassiveListDef<String> lore = null;
	public List<String> getLore() { return this.lore; }
	public ItemData setLore(Collection<String> lore) { this.lore = (lore == null ? null : new MassiveListDef<>(lore)); return this;}
	
	// TODO: Can I create a string comparator and use that one instead? HashCode looks ugly.
	private MassiveTreeMapDef<String, Integer, ComparatorHashCode> enchants = null;
	public Map<String, Integer> getEnchants() { return this.enchants; }
	public ItemData setEnchants(Map<String, Integer> enchants) { this.enchants = (enchants == null ? null : new MassiveTreeMapDef<String, Integer, ComparatorHashCode>(ComparatorHashCode.get(), enchants)); return this; }
	
	private Integer repaircost = null;
	public Integer getRepaircost() { return this.repaircost; }
	public ItemData setRepaircost(int repaircost) { this.repaircost = repaircost; return this; }
	
	// -------------------------------------------- //
	// FIELDS > BOOK
	// -------------------------------------------- //
	
	private String title = null;
	public String getTitle() { return this.title; }
	public ItemData setTitle(String title) { this.title = title; return this; }
	
	private String author = null;
	public String getAuthor() { return this.author; }
	public ItemData setAuthor(String author) { this.author = author; return this; }
	
	private MassiveListDef<String> pages = new MassiveListDef<>();
	public List<String> getPages() { return this.pages; }
	public ItemData setPages(Collection<String> bookPages) { this.pages = new MassiveListDef<>(bookPages); return this; }
	
	// -------------------------------------------- //
	// FIELDS > LEATHER ARMOR
	// -------------------------------------------- //
	
	// TODO: Color is not a primitive... convert into using a primitive!
	// TODO: We must also figure out the int value of the DefaultLeatherColor!
	private Color color = null;
	public Color getColor() { return (this.color == null ? Bukkit.getItemFactory().getDefaultLeatherColor() : this.color); }
	public ItemData setColor(Color color) { this.color = (Bukkit.getItemFactory().getDefaultLeatherColor().equals(color) ? null : color); return this; }
	
	// -------------------------------------------- //
	// FIELDS > MAP
	// -------------------------------------------- //
	
	private Boolean scaling = null;
	public boolean isScaling() { return (this.scaling == null ? DEFAULT_SCALING : this.scaling); }
	public ItemData setScaling(boolean scaling) { this.scaling = (scaling == DEFAULT_SCALING ? null : scaling); return this; }
	
	// -------------------------------------------- //
	// FIELDS > POTION
	// -------------------------------------------- //
	
	// TODO: Create and use PotionEffectData!
	@SerializedName("potion-effects")
	private List<PotionEffect> potionEffects = null;
	public List<PotionEffect> getPotionEffects() { return this.potionEffects; }
	public ItemData setPotionEffects(Collection<PotionEffect> potionEffects) { this.potionEffects = (potionEffects == null ? null : new MassiveListDef<>(potionEffects)); return this; }
	
	// -------------------------------------------- //
	// FIELDS > SKULL
	// -------------------------------------------- //
	
	private String skull = null;
	public String getSkull() { return this.skull; }
	public ItemData setSkull(String skull) { this.skull = skull; return this; }
	
	// -------------------------------------------- //
	// FIELDS > FIREWORK EFFECT
	// -------------------------------------------- //
	
	// TODO
	
	// -------------------------------------------- //
	// FIELDS > FIREWORK
	// -------------------------------------------- //
	
	// TODO
	
	// -------------------------------------------- //
	// FIELDS > STORED ENCHANTS
	// -------------------------------------------- //
	
	// TODO: Can I create a string comparator and use that one instead? HashCode looks ugly.
	@SerializedName("stored-enchants")
	private MassiveTreeMapDef<String, Integer, ComparatorHashCode> storedEnchants = null;
	public Map<String, Integer> getStoredEnchants() { return this.storedEnchants; }
	public ItemData setStoredEnchants(Map<String, Integer> storedEnchants) { this.storedEnchants = (storedEnchants == null ? null : new MassiveTreeMapDef<String, Integer, ComparatorHashCode>(ComparatorHashCode.get(), storedEnchants)); return this; }
	
	// -------------------------------------------- //
	// FIELDS > UNBREAKABLE
	// -------------------------------------------- //
	// SINCE: 1.8
	
	private Boolean unbreakable = null;
	public boolean isUnbreakable() { return (this.unbreakable == null ? DEFAULT_UNBREAKABLE : this.unbreakable); }
	public ItemData setUnbreakable(boolean unbreakable) { this.unbreakable = (unbreakable == DEFAULT_UNBREAKABLE ? null : unbreakable); return this; }
	
	// -------------------------------------------- //
	// FIELDS > FLAGS
	// -------------------------------------------- //
	// SINCE: 1.8
	
	// TODO: Can I create a string comparator and use that one instead? HashCode looks ugly.
	private MassiveTreeSetDef<String, ComparatorHashCode> flags = null;
	public Set<String> getFlags() { return this.flags; }
	public ItemData setFlags(Collection<String> flags) { this.flags = (flags == null ? null : new MassiveTreeSetDef<String, ComparatorHashCode>(ComparatorHashCode.get(), flags)); return this; }
	
	// -------------------------------------------- //
	// FIELDS > BANNER BASE
	// -------------------------------------------- //
	// SINCE: 1.8
	// The integer is the dye color byte representation.
	
	@SerializedName("banner-base")
	private Integer bannerBase = null;
	public Integer getBannerBase() { return this.bannerBase; }
	public ItemData setBannerBase(Integer bannerBase) { this.bannerBase = bannerBase; return this; }
	
	// -------------------------------------------- //
	// FIELDS > BANNER PATTERNS
	// -------------------------------------------- //
	// SINCE: 1.8
	// This should really be a list and not a set.
	// The order matters and is explicitly assigned.
	
	// TODO: The Pattern class can not be used here. It breaks 1.8 compatibility.
	// TODO: Convert to to use only raw primitiveish data!
	// TODO: I actually decided to use a list of integers. That should be mimiced here.
	@SerializedName("banner")
	private MassiveListDef<Pattern> bannerPatterns = null;
	public List<Pattern> getBannerPatterns() { return this.bannerPatterns; }
	public ItemData setBannerPatterns(Collection<Pattern> bannerPatterns) { this.bannerPatterns = (bannerPatterns == null ? null : new MassiveListDef<>(bannerPatterns)); return this;}
	
	// -------------------------------------------- //
	// FIELDS > POTION
	// -------------------------------------------- //
	// SINCE: 1.9
	
	private String potion = null;
	public String getPotion() { return this.potion; }
	public ItemData setPotion(String potion) { this.potion = potion; return this; }
	
	// -------------------------------------------- //
	// CONVERT
	// -------------------------------------------- //
	
	// TODO: Add in covert methods... they will have to use mixins for transfering!
	
	// -------------------------------------------- //
	// HASH CODE & EQUALS
	// -------------------------------------------- //
	
	// TODO
	
}
