package com.massivecraft.massivecore.adapter;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.item.DataItemStack;
import com.massivecraft.massivecore.mixin.MixinInventory;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializer;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonObject;
import com.massivecraft.massivecore.xlib.gson.JsonParseException;
import com.massivecraft.massivecore.xlib.gson.JsonPrimitive;
import com.massivecraft.massivecore.xlib.gson.JsonSerializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonSerializer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map.Entry;

/**
 * This is my Gson adapter for Inventories.
 * It handles all inventories as CraftInventoryCustom "Chest"s with size of your choice
 * except for PlayerInventory which it handles pretty darn well!
 */
public class AdapterInventory implements JsonDeserializer<Inventory>, JsonSerializer<Inventory>
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final int SIZE_PLAYER_STORAGE = 36;
	public static final int INDEX_PLAYER_STORAGE_FROM = 0;
	public static final int INDEX_PLAYER_STORAGE_TO = SIZE_PLAYER_STORAGE - 1;
	
	public static final int INDEX_PLAYER_SHIELD = 40;
	
	// -------------------------------------------- //
	// FIELD NAME CONSTANTS
	// -------------------------------------------- //
	
	public static final String SIZE = "size";
	
	public static final String PLAYER = "player";
	
	public static final String HELMET = "helmet";
	public static final String CHESTPLATE = "chestplate";
	public static final String LEGGINGS = "leggings";
	public static final String BOOTS = "boots";
	public static final String SHIELD = "shield";
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final AdapterInventory i = new AdapterInventory();
	public static AdapterInventory get() { return i; }

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public JsonElement serialize(Inventory src, Type typeOfSrc, JsonSerializationContext context)
	{
		return toJson(src);
	}
	
	@Override
	public Inventory deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		return fromJson(json);
	}
	
	// -------------------------------------------- //
	// IMPLEMENTATION
	// -------------------------------------------- //
	
	public static JsonElement toJson(Inventory src)
	{
		// The return value is this object:
		JsonObject jsonInventory = new JsonObject();
		
		// These variables are used in loops and repetitive logic.
		ItemStack itemStack = null;
		JsonElement jsonItemStack = null;
		String index = null;
		
		// Every inventory has a content part.
		ItemStack[] itemStacks = src.getContents();
		
		if (src instanceof PlayerInventory)
		{
			// Add the size "player"
			jsonInventory.addProperty(SIZE, PLAYER);
			
			// Cast to PlayerInventory
			PlayerInventory psrc = (PlayerInventory)src;
			
			// Helmet
			itemStack = psrc.getHelmet();
			if (itemStack != null)
			{
				jsonItemStack = MassiveCore.gson.toJsonTree(itemStack, ItemStack.class);
				jsonInventory.add(HELMET, jsonItemStack);
			}
			
			// Chestplate
			itemStack = psrc.getChestplate();
			if (itemStack != null)
			{
				jsonItemStack = MassiveCore.gson.toJsonTree(itemStack, ItemStack.class);
				jsonInventory.add(CHESTPLATE, jsonItemStack);
			}
			
			// Leggings
			itemStack = psrc.getLeggings();
			if (itemStack != null)
			{
				jsonItemStack = MassiveCore.gson.toJsonTree(itemStack, ItemStack.class);
				jsonInventory.add(LEGGINGS, jsonItemStack);
			}
			
			// Boots
			itemStack = psrc.getBoots();
			if (itemStack != null)
			{
				jsonItemStack = MassiveCore.gson.toJsonTree(itemStack, ItemStack.class);
				jsonInventory.add(BOOTS, jsonItemStack);
			}
			
			// Shield (Minecraft 1.9)
			itemStack = null;
			if (INDEX_PLAYER_SHIELD < itemStacks.length) itemStack = itemStacks[INDEX_PLAYER_SHIELD];
			if (itemStack != null)
			{
				jsonItemStack = MassiveCore.gson.toJsonTree(itemStack, ItemStack.class);
				jsonInventory.add(SHIELD, jsonItemStack);
			}
			
			// Storage Range (Minecraft 1.9)
			itemStacks = range(itemStacks, INDEX_PLAYER_STORAGE_FROM, INDEX_PLAYER_STORAGE_TO);
		}
		else
		{
			// Add the size *length*
			jsonInventory.addProperty(SIZE, itemStacks.length);
		}
		
		// Add the content at the end since we like to have it at the bottom of return json.
		for (Entry<Integer, DataItemStack> entry : DataItemStack.fromBukkitContents(itemStacks).entrySet())
		{
			index = String.valueOf(entry.getKey());
			jsonItemStack = MassiveCore.gson.toJsonTree(entry.getValue());
			jsonInventory.add(index, jsonItemStack);
		}
		
		return jsonInventory;
	}
	
	public static Inventory fromJson(JsonElement json)
	{
		// If must be an object!
		if ( ! json.isJsonObject()) return null;
		JsonObject jsonInventory = json.getAsJsonObject();
		
		// The return value
		Inventory ret = null;
		
		// These variables are used in loops and repetitive logic.
		ItemStack itemStack = null;
		JsonElement jsonItemStack = null;
		
		// There must be a size entry!
		if ( ! jsonInventory.has(SIZE)) return null;
		
		JsonPrimitive jsonSize = jsonInventory.get(SIZE).getAsJsonPrimitive();
		int size = 0;
		
		// What size/type is it?
		if (jsonSize.isString() && jsonSize.getAsString().equals(PLAYER))
		{
			// We use 36 here since it's the size of the player inventory (without armor)
			size = SIZE_PLAYER_STORAGE;
			
			// This is a PlayerInventory
			ret = MixinInventory.get().createPlayerInventory();
			PlayerInventory pret = (PlayerInventory)ret;
			
			// Helmet
			if (jsonInventory.has(HELMET))
			{
				jsonItemStack = jsonInventory.get(HELMET);
				itemStack = MassiveCore.gson.fromJson(jsonItemStack, ItemStack.class);
				pret.setHelmet(itemStack);
			}
			
			// Chestplate
			if (jsonInventory.has(CHESTPLATE))
			{
				jsonItemStack = jsonInventory.get(CHESTPLATE);
				itemStack = MassiveCore.gson.fromJson(jsonItemStack, ItemStack.class);
				pret.setChestplate(itemStack);
			}
			
			// Leggings
			if (jsonInventory.has(LEGGINGS))
			{
				jsonItemStack = jsonInventory.get(LEGGINGS);
				itemStack = MassiveCore.gson.fromJson(jsonItemStack, ItemStack.class);
				pret.setLeggings(itemStack);
			}
			
			// Boots
			if (jsonInventory.has(BOOTS))
			{
				jsonItemStack = jsonInventory.get(BOOTS);
				itemStack = MassiveCore.gson.fromJson(jsonItemStack, ItemStack.class);
				pret.setBoots(itemStack);
			}
			
			// Shield (Minecraft 1.9)
			if (jsonInventory.has(SHIELD) && INDEX_PLAYER_SHIELD < pret.getSize())
			{
				jsonItemStack = jsonInventory.get(SHIELD);
				itemStack = MassiveCore.gson.fromJson(jsonItemStack, ItemStack.class);
				pret.setItem(INDEX_PLAYER_SHIELD, itemStack);
			}
		}
		else
		{
			// A custom size were specified
			size = jsonSize.getAsInt();
			
			// This is a "Custom" Inventory (content only).
			ret = MixinInventory.get().createInventory(null, size, "");
		}
		
		// Now process content
		for (int i = 0; i < size; i++)
		{
			String stackIdx = String.valueOf(i);
			jsonItemStack = jsonInventory.get(stackIdx);
			if (jsonItemStack == null) continue;
			itemStack = MassiveCore.gson.fromJson(jsonItemStack, ItemStack.class);
			if (itemStack == null) continue;
			ret.setItem(i, itemStack);
		}
		
		return ret;
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	// This is a modified copyOfRange implementation.
	// Both boundaries are inclusive.
	// It returns the original when possible.
	public static <T> T[] range(T[] original, int from, int to)
	{
		if (from == 0 && to == original.length - 1) return original;
		return Arrays.copyOfRange(original, from, to + 1);
	}
	
}
