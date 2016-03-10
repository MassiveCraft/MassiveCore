package com.massivecraft.massivecore.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;

import com.massivecraft.massivecore.xlib.gson.JsonArray;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonObject;
import com.massivecraft.massivecore.xlib.gson.JsonPrimitive;

@SuppressWarnings("deprecation")
public class ItemStackAdapterInner17 implements ItemStackAdapterInner
{
	// -------------------------------------------- //
	// CONSTANTS: NAMES
	// -------------------------------------------- //

	public static final String ID = "id";
	public static final String COUNT = "count";
	public static final String DAMAGE = "damage";

	public static final String NAME = "name";
	public static final String LORE = "lore";

	public static final String ENCHANTS = "enchants";

	public static final String REPAIRCOST = "repaircost";

	public static final String BOOK_TITLE = "title";
	public static final String BOOK_AUTHOR = "author";
	public static final String BOOK_PAGES = "pages";

	public static final String LEATHER_ARMOR_COLOR = "color";

	public static final String MAP_SCALING = "scaling";

	public static final String SKULL_OWNER = "skull";

	// We renamed "effects" to "potion-effects".
	public static final String POTION_EFFECTS_OLD = "effects";
	public static final String POTION_EFFECTS = "potion-effects";

	public static final String FIREWORK_EFFECT = "firework-effect";
	public static final String FIREWORK_EFFECTS = "firework-effects";
	public static final String FIREWORK_FLIGHT = "firework-flight";

	public static final String STORED_ENCHANTS = "stored-enchants";

	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	public static ItemStackAdapterInner17 i = new ItemStackAdapterInner17();
	public static ItemStackAdapterInner17 get() { return i; }
	public ItemStackAdapterInner17()
	{
		this.provoke();
	}

	// -------------------------------------------- //
	// PROVOKE
	// -------------------------------------------- //
	
	@Override
	public Object provoke()
	{
		return null;
	}
	
	// -------------------------------------------- //
	// WRITE
	// -------------------------------------------- //

	@Override
	public JsonObject erialize(ItemStack stack)
	{
		// Check for "nothing"
		if (stack == null) return null;
		if (stack.getType() == Material.AIR) return null;
		if (stack.getAmount() == 0) return null;

		// Create a new JsonObject
		JsonObject json = new JsonObject();

		// Transfer data from stack to json
		this.transferAll(stack, json, true);

		return json;
	}

	@Override
	public ItemStack erialize(JsonElement jsonElement)
	{
		// Check for "nothing"
		if (jsonElement == null) return null;

		// Must be a JsonObject
		if (jsonElement.isJsonObject() == false) return null;
		JsonObject json = jsonElement.getAsJsonObject();

		// Create a new ItemStack
		ItemStack stack = createItemStack();

		// Transfer data from json to stack
		this.transferAll(stack, json, false);

		return stack;
	}

	// -------------------------------------------- //
	// NOARG STACK CONSTRUCTOR
	// -------------------------------------------- //

	public static final int DEFAULT_ID = 0;
	public static final int DEFAULT_COUNT = 1;
	public static final int DEFAULT_DAMAGE = 0;
	public ItemStack createItemStack()
	{
		return new ItemStack(DEFAULT_ID, DEFAULT_COUNT, (short)DEFAULT_DAMAGE);
	}

	// -------------------------------------------- //
	// ALL
	// -------------------------------------------- //

	public void transferAll(ItemStack stack, JsonObject json, boolean stack2json)
	{
		this.transferBasic(stack, json, stack2json);

		ItemMeta meta = stack.getItemMeta();
		this.transferMeta(stack, json, stack2json, meta);

		if (stack2json == false)
		{
			stack.setItemMeta(meta);
		}
	}

	// -------------------------------------------- //
	// BASIC
	// -------------------------------------------- //

	public void transferBasic(ItemStack stack, JsonObject json, boolean stack2json)
	{
		this.transferId(stack, json, stack2json);
		this.transferCount(stack, json, stack2json);
		this.transferDamage(stack, json, stack2json);
	}

	// -------------------------------------------- //
	// BASIC: ID
	// -------------------------------------------- //

	public void transferId(ItemStack stack, JsonObject json, boolean stack2json)
	{
		if (stack2json)
		{
			int id = stack.getTypeId();
			if (id == DEFAULT_ID) return;
			json.addProperty(ID, id);
		}
		else
		{
			JsonElement element = json.get(ID);
			if (element == null) return;
			stack.setTypeId(element.getAsInt());
		}
	}

	// -------------------------------------------- //
	// BASIC: COUNT
	// -------------------------------------------- //

	public void transferCount(ItemStack stack, JsonObject json, boolean stack2json)
	{
		if (stack2json)
		{
			int count = stack.getAmount();
			if (count == DEFAULT_COUNT) return;
			json.addProperty(COUNT, count);
		}
		else
		{
			JsonElement element = json.get(COUNT);
			if (element == null) return;
			stack.setAmount(element.getAsInt());
		}
	}

	// -------------------------------------------- //
	// BASIC: DAMAGE
	// -------------------------------------------- //

	public void transferDamage(ItemStack stack, JsonObject json, boolean stack2json)
	{
		// Durability is a weird name since it is the amount of damage.
		if (stack2json)
		{
			int damage = stack.getDurability();
			if (damage == DEFAULT_DAMAGE) return;
			json.addProperty(DAMAGE, damage);
		}
		else
		{
			JsonElement element = json.get(DAMAGE);
			if (element == null) return;
			stack.setDurability(element.getAsShort());
		}
	}

	// -------------------------------------------- //
	// META
	// -------------------------------------------- //

	public void transferMeta(ItemStack stack, JsonObject json, boolean meta2json, ItemMeta meta)
	{
		this.transferMetaUnspecific(stack, json, meta2json, meta);
		this.transferMetaSpecific(stack, json, meta2json, meta);
	}

	// -------------------------------------------- //
	// UNSPECIFIC META
	// -------------------------------------------- //

	public void transferMetaUnspecific(ItemStack stack, JsonObject json, boolean meta2json, ItemMeta meta)
	{
		this.transferName(stack, json, meta2json, meta);
		this.transferLore(stack, json, meta2json, meta);
		this.transferEnchants(stack, json, meta2json, meta);
		this.transferRepaircost(stack, json, meta2json, meta);
	}

	// -------------------------------------------- //
	// UNSPECIFIC META: NAME
	// -------------------------------------------- //

	public void transferName(ItemStack stack, JsonObject json, boolean meta2json, ItemMeta meta)
	{
		if (meta2json)
		{
			if (!meta.hasDisplayName()) return;
			json.addProperty(NAME, meta.getDisplayName());
		}
		else
		{
			JsonElement element = json.get(NAME);
			if (element == null) return;
			meta.setDisplayName(element.getAsString());
		}
	}

	// -------------------------------------------- //
	// UNSPECIFIC META: LORE
	// -------------------------------------------- //

	public void transferLore(ItemStack stack, JsonObject json, boolean meta2json, ItemMeta meta)
	{
		if (meta2json)
		{
			if (!meta.hasLore()) return;
			json.add(LORE, convertStringList(meta.getLore()));
		}
		else
		{
			JsonElement element = json.get(LORE);
			if (element == null) return;
			meta.setLore(convertStringList(element));
		}
	}

	// -------------------------------------------- //
	// UNSPECIFIC META: ENCHANTS
	// -------------------------------------------- //

	public void transferEnchants(ItemStack stack, JsonObject json, boolean meta2json, ItemMeta meta)
	{
		if (meta2json)
		{
			if (!meta.hasEnchants()) return;
			json.add(ENCHANTS, convertEnchantLevelMap(meta.getEnchants()));
		}
		else
		{
			JsonElement element = json.get(ENCHANTS);
			if (element == null) return;
			for (Entry<Enchantment, Integer> entry : convertEnchantLevelMap(element).entrySet())
			{
				meta.addEnchant(entry.getKey(), entry.getValue(), true);
			}
		}
	}

	// -------------------------------------------- //
	// UNSPECIFIC META: REPAIRCOST
	// -------------------------------------------- //

	public void transferRepaircost(ItemStack stack, JsonObject json, boolean meta2json, ItemMeta meta)
	{
		if ( ! (meta instanceof Repairable)) return;
		Repairable repairable = (Repairable) meta;

		if (meta2json)
		{
			if (!repairable.hasRepairCost()) return;
			json.addProperty(REPAIRCOST, repairable.getRepairCost());
		}
		else
		{
			JsonElement element = json.get(REPAIRCOST);
			if (element == null) return;

			repairable.setRepairCost(element.getAsInt());
		}
	}

	// -------------------------------------------- //
	// SPECIFIC META
	// -------------------------------------------- //

	public void transferMetaSpecific(ItemStack stack, JsonObject json, boolean meta2json, ItemMeta meta)
	{
		if (meta instanceof BookMeta)
		{
			this.transferBook(stack, json, meta2json, (BookMeta)meta);
		}
		else if (meta instanceof LeatherArmorMeta)
		{
			this.transferLeatherArmor(stack, json, meta2json, (LeatherArmorMeta)meta);
		}
		else if (meta instanceof MapMeta)
		{
			this.transferMap(stack, json, meta2json, (MapMeta)meta);
		}
		else if (meta instanceof PotionMeta)
		{
			this.transferPotion(stack, json, meta2json, (PotionMeta)meta);
		}
		else if (meta instanceof SkullMeta)
		{
			this.transferSkull(stack, json, meta2json, (SkullMeta)meta);
		}
		else if (meta instanceof FireworkEffectMeta)
		{
			this.transferFireworkEffect(stack, json, meta2json, (FireworkEffectMeta)meta);
		}
		else if (meta instanceof FireworkMeta)
		{
			this.transferFirework(stack, json, meta2json, (FireworkMeta)meta);
		}
		else if (meta instanceof EnchantmentStorageMeta)
		{
			this.transferEnchantmentStorage(stack, json, meta2json, (EnchantmentStorageMeta)meta);
		}
	}

	// -------------------------------------------- //
	// SPECIFIC META: BOOK
	// -------------------------------------------- //

	public void transferBook(ItemStack stack, JsonObject json, boolean meta2json, BookMeta meta)
	{
		this.transferTitle(stack, json, meta2json, meta);
		this.transferAuthor(stack, json, meta2json, meta);
		this.transferPages(stack, json, meta2json, meta);
	}

	public void transferTitle(ItemStack stack, JsonObject json, boolean meta2json, BookMeta meta)
	{
		if (meta2json)
		{
			if (!meta.hasTitle()) return;
			json.addProperty(BOOK_TITLE, meta.getTitle());
		}
		else
		{
			JsonElement element = json.get(BOOK_TITLE);
			if (element == null) return;
			meta.setTitle(element.getAsString());
		}
	}

	public void transferAuthor(ItemStack stack, JsonObject json, boolean meta2json, BookMeta meta)
	{
		if (meta2json)
		{
			if (!meta.hasTitle()) return;
			json.addProperty(BOOK_AUTHOR, meta.getAuthor());
		}
		else
		{
			JsonElement element = json.get(BOOK_AUTHOR);
			if (element == null) return;
			meta.setAuthor(element.getAsString());
		}
	}

	public void transferPages(ItemStack stack, JsonObject json, boolean meta2json, BookMeta meta)
	{
		if (meta2json)
		{
			if (!meta.hasTitle()) return;
			try
			{
				json.add(BOOK_PAGES, convertStringList(meta.getPages()));
			}
			catch (Exception e)
			{
				e.printStackTrace();
				// It seems CraftMetaBook#getPages some times throw an NPE.
			}
		}
		else
		{
			JsonElement element = json.get(BOOK_PAGES);
			if (element == null) return;
			meta.setPages(convertStringList(element));
		}
	}

	// -------------------------------------------- //
	// SPECIFIC META: LEATHER ARMOR
	// -------------------------------------------- //

	public void transferLeatherArmor(ItemStack stack, JsonObject json, boolean meta2json, LeatherArmorMeta meta)
	{
		if (meta2json)
		{
			Color color = meta.getColor();
			
			if (Bukkit.getItemFactory().getDefaultLeatherColor().equals(color)) return;

			json.addProperty(LEATHER_ARMOR_COLOR, color.asRGB());
		}
		else
		{
			JsonElement element = json.get(LEATHER_ARMOR_COLOR);
			if (element == null) return;
			meta.setColor(Color.fromRGB(element.getAsInt()));
		}
	}

	// -------------------------------------------- //
	// SPECIFIC META: MAP
	// -------------------------------------------- //

	public void transferMap(ItemStack stack, JsonObject json, boolean meta2json, MapMeta meta)
	{
		if (meta2json)
		{
			if (!meta.isScaling()) return;
			json.addProperty(MAP_SCALING, true);
		}
		else
		{
			JsonElement element = json.get(MAP_SCALING);
			if (element == null) return;

			meta.setScaling(element.getAsBoolean());
		}
	}

	// -------------------------------------------- //
	// SPECIFIC META: POTION
	// -------------------------------------------- //

	public void transferPotion(ItemStack stack, JsonObject json, boolean meta2json, PotionMeta meta)
	{
		if (meta2json)
		{
			if (!meta.hasCustomEffects()) return;
			json.add(POTION_EFFECTS, convertPotionEffectList(meta.getCustomEffects()));
		}
		else
		{
			JsonElement element = json.get(POTION_EFFECTS);
			if (element == null) element = json.get(POTION_EFFECTS_OLD);
			if (element == null) return;

			meta.clearCustomEffects();
			for (PotionEffect pe : convertPotionEffectList(element))
			{
				meta.addCustomEffect(pe, false);
			}
		}
	}

	// -------------------------------------------- //
	// SPECIFIC META: SKULL
	// -------------------------------------------- //

	public void transferSkull(ItemStack stack, JsonObject json, boolean meta2json, SkullMeta meta)
	{
		if (meta2json)
		{
			if (!meta.hasOwner()) return;
			json.addProperty(SKULL_OWNER, meta.getOwner());
		}
		else
		{
			JsonElement element = json.get(SKULL_OWNER);
			if (element == null) return;
			meta.setOwner(element.getAsString());
		}
	}

	// -------------------------------------------- //
	// SPECIFIC META: FIREWORK EFFECT
	// -------------------------------------------- //

	public void transferFireworkEffect(ItemStack stack, JsonObject json, boolean meta2json, FireworkEffectMeta meta)
	{
		if (meta2json)
		{
			if (!meta.hasEffect()) return;
			json.add(FIREWORK_EFFECT, FireworkEffectAdapter.toJson(meta.getEffect()));
		}
		else
		{
			JsonElement element = json.get(FIREWORK_EFFECT);
			if (element == null) return;
			meta.setEffect(FireworkEffectAdapter.fromJson(element));
		}
	}

	// -------------------------------------------- //
	// SPECIFIC META: FIREWORK
	// -------------------------------------------- //

	public void transferFirework(ItemStack stack, JsonObject json, boolean meta2json, FireworkMeta meta)
	{
		this.transferFireworkEffects(stack, json, meta2json, meta);
		this.transferFireworkPower(stack, json, meta2json, meta);
	}
	
	public void transferFireworkEffects(ItemStack stack, JsonObject json, boolean meta2json, FireworkMeta meta)
	{
		if (meta2json)
		{
			if (!meta.hasEffects()) return;
			json.add(FIREWORK_EFFECTS, convertFireworkEffectList(meta.getEffects()));
		}
		else
		{
			JsonElement element = json.get(FIREWORK_EFFECTS);
			if (element == null) return;
			meta.clearEffects();
			meta.addEffects(convertFireworkEffectList(element));
		}
	}
	
	public void transferFireworkPower(ItemStack stack, JsonObject json, boolean meta2json, FireworkMeta meta)
	{
		if (meta2json)
		{
			json.addProperty(FIREWORK_FLIGHT, meta.getPower());
		}
		else
		{
			JsonElement element = json.get(FIREWORK_FLIGHT);
			if (element == null) return;
			meta.setPower(element.getAsInt());
		}
	}

	// -------------------------------------------- //
	// SPECIFIC META: ENCHANTMENT STORAGE
	// -------------------------------------------- //

	public void transferEnchantmentStorage(ItemStack stack, JsonObject json, boolean meta2json, EnchantmentStorageMeta meta)
	{
		if (meta2json)
		{
			if (!meta.hasStoredEnchants()) return;
			json.add(STORED_ENCHANTS, convertEnchantLevelMap(meta.getStoredEnchants()));
		}
		else
		{
			JsonElement element = json.get(STORED_ENCHANTS);
			if (element == null) return;
			for (Entry<Enchantment, Integer> entry : convertEnchantLevelMap(element).entrySet())
			{
				meta.addStoredEnchant(entry.getKey(), entry.getValue(), true);
			}
		}
	}

	// -------------------------------------------- //
	// MINI UTILS
	// -------------------------------------------- //

	// String List
	public static JsonArray convertStringList(Collection<String> strings)
	{
		JsonArray ret = new JsonArray();
		for (String string : strings)
		{
			ret.add(new JsonPrimitive(string));
		}
		return ret;
	}

	public static List<String> convertStringList(JsonElement jsonElement)
	{
		JsonArray array = jsonElement.getAsJsonArray();
		List<String> ret = new ArrayList<String>();

		Iterator<JsonElement> iter = array.iterator();
		while (iter.hasNext())
		{
			JsonElement element = iter.next();
			ret.add(element.getAsString());
		}

		return ret;
	}
	
	// PotionEffect List
	public static JsonArray convertPotionEffectList(Collection<PotionEffect> potionEffects)
	{
		JsonArray ret = new JsonArray();
		for (PotionEffect e : potionEffects)
		{
			ret.add(PotionEffectAdapter.toJson(e));
		}
		return ret;
	}
	
	public static List<PotionEffect> convertPotionEffectList(JsonElement jsonElement)
	{
		if (jsonElement == null) return null;
		if ( ! jsonElement.isJsonArray()) return null;
		JsonArray array = jsonElement.getAsJsonArray();
		
		List<PotionEffect> ret = new ArrayList<PotionEffect>();
		
		Iterator<JsonElement> iter = array.iterator();
		while(iter.hasNext())
		{
			PotionEffect e = PotionEffectAdapter.fromJson(iter.next());
			if (e == null) continue;
			ret.add(e);
		}
		
		return ret;
	}
	
	// FireworkEffect List
	public static JsonArray convertFireworkEffectList(Collection<FireworkEffect> fireworkEffects)
	{
		JsonArray ret = new JsonArray();
		for (FireworkEffect fe : fireworkEffects)
		{
			ret.add(FireworkEffectAdapter.toJson(fe));
		}
		return ret;
	}
	
	public static List<FireworkEffect> convertFireworkEffectList(JsonElement jsonElement)
	{
		if (jsonElement == null) return null;
		if ( ! jsonElement.isJsonArray()) return null;
		JsonArray array = jsonElement.getAsJsonArray();
		
		List<FireworkEffect> ret = new ArrayList<FireworkEffect>();
		
		Iterator<JsonElement> iter = array.iterator();
		while(iter.hasNext())
		{
			FireworkEffect fe = FireworkEffectAdapter.fromJson(iter.next());
			if (fe == null) continue;
			ret.add(fe);
		}
		
		return ret;
	}
	
	// EnchantLevelMap
	public static JsonObject convertEnchantLevelMap(Map<Enchantment, Integer> enchantLevelMap)
	{
		JsonObject ret = new JsonObject();
		for (Entry<Enchantment, Integer> entry : enchantLevelMap.entrySet())
		{
			ret.addProperty(String.valueOf(entry.getKey().getId()), entry.getValue());
		}
		return ret;
	}
	
	public static Map<Enchantment, Integer> convertEnchantLevelMap(JsonElement jsonElement)
	{
		JsonObject json = jsonElement.getAsJsonObject();
		Map<Enchantment, Integer> ret = new HashMap<Enchantment, Integer>();
		for (Entry<String, JsonElement> entry : json.entrySet())
		{
			int id = Integer.valueOf(entry.getKey());
			Enchantment ench = Enchantment.getById(id);
			int lvl = entry.getValue().getAsInt();
			ret.put(ench, lvl);
		}
		return ret;
	}

}
