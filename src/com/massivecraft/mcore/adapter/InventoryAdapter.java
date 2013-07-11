package com.massivecraft.mcore.adapter;

import java.lang.reflect.Type;

import org.bukkit.craftbukkit.v1_6_R2.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_6_R2.inventory.CraftInventoryPlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.inventory.MCorePlayerInventory;
import com.massivecraft.mcore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.mcore.xlib.gson.JsonDeserializer;
import com.massivecraft.mcore.xlib.gson.JsonElement;
import com.massivecraft.mcore.xlib.gson.JsonObject;
import com.massivecraft.mcore.xlib.gson.JsonParseException;
import com.massivecraft.mcore.xlib.gson.JsonPrimitive;
import com.massivecraft.mcore.xlib.gson.JsonSerializationContext;
import com.massivecraft.mcore.xlib.gson.JsonSerializer;

/**
 * This is my Gson adapter for Inventories.
 * It handles all inventories as CraftInventoryCustom "Chest"s with size of your choice
 * except for PlayerInventory which it handles pretty darn well!
 */
public class InventoryAdapter implements JsonDeserializer<Inventory>, JsonSerializer<Inventory>
{
	// -------------------------------------------- //
	// FIELD NAME CONSTANTS
	// -------------------------------------------- //
	
	public static final String SIZE = "size";
	
	public static final String PLAYER = "player";
	
	public static final String HELMET = "helmet";
	public static final String CHESTPLATE = "chestplate";
	public static final String LEGGINGS = "leggings";
	public static final String BOOTS = "boots";
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static InventoryAdapter i = new InventoryAdapter();
	public static InventoryAdapter get() { return i; }

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
		
		// Every inventory has a content part.
		ItemStack[] itemStacks = src.getContents();
		
		if (src instanceof PlayerInventory)
		{
			// Add the size "player"
			jsonInventory.addProperty(SIZE, PLAYER);
			
			// Cast to PlayerInventory
			PlayerInventory psrc = (PlayerInventory)src;
			
			// helmet
			itemStack = psrc.getHelmet();
			if (itemStack != null)
			{
				jsonItemStack = MCore.gson.toJsonTree(itemStack, ItemStack.class);
				jsonInventory.add(HELMET, jsonItemStack);
			}
			
			// chestplate
			itemStack = psrc.getChestplate();
			if (itemStack != null)
			{
				jsonItemStack = MCore.gson.toJsonTree(itemStack, ItemStack.class);
				jsonInventory.add(CHESTPLATE, jsonItemStack);
			}
			
			// leggings
			itemStack = psrc.getLeggings();
			if (itemStack != null)
			{
				jsonItemStack = MCore.gson.toJsonTree(itemStack, ItemStack.class);
				jsonInventory.add(LEGGINGS, jsonItemStack);
			}
			
			// boots
			itemStack = psrc.getBoots();
			if (itemStack != null)
			{
				jsonItemStack = MCore.gson.toJsonTree(itemStack, ItemStack.class);
				jsonInventory.add(BOOTS, jsonItemStack);
			}
		}
		else
		{
			// Add the size *length*
			jsonInventory.addProperty(SIZE, itemStacks.length);
		}
		
		// Add the content at the end since we like to have it at the bottom of return json.
		for (int i = 0; i < itemStacks.length; i++)
		{
			itemStack = itemStacks[i];
			jsonItemStack = MCore.gson.toJsonTree(itemStack, ItemStack.class);
			if (jsonItemStack == null) continue;
			jsonInventory.add(String.valueOf(i), jsonItemStack);
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
			size = 36;
			
			// This is a PlayerInventory
			ret = new CraftInventoryPlayer(new MCorePlayerInventory());
			PlayerInventory pret = (PlayerInventory)ret;
			
			// helmet
			if (jsonInventory.has(HELMET))
			{
				jsonItemStack = jsonInventory.get(HELMET);
				itemStack = MCore.gson.fromJson(jsonItemStack, ItemStack.class);
				pret.setHelmet(itemStack);
			}
			
			// chestplate
			if (jsonInventory.has(CHESTPLATE))
			{
				jsonItemStack = jsonInventory.get(CHESTPLATE);
				itemStack = MCore.gson.fromJson(jsonItemStack, ItemStack.class);
				pret.setChestplate(itemStack);
			}
			
			// leggings
			if (jsonInventory.has(LEGGINGS))
			{
				jsonItemStack = jsonInventory.get(LEGGINGS);
				itemStack = MCore.gson.fromJson(jsonItemStack, ItemStack.class);
				pret.setLeggings(itemStack);
			}
			
			// boots
			if (jsonInventory.has(BOOTS))
			{
				jsonItemStack = jsonInventory.get(BOOTS);
				itemStack = MCore.gson.fromJson(jsonItemStack, ItemStack.class);
				pret.setBoots(itemStack);
			}
		}
		else
		{
			// A custom size were specified
			size = jsonSize.getAsInt();
			
			// This is a "Custom" Inventory (content only).
			ret = new CraftInventoryCustom(null, size);
		}
		
		// Now process content
		ItemStack[] itemStacks = new ItemStack[size];
		for (int i = 0; i < size; i++)
		{
			// Fetch the jsonItemStack or mark it as empty and continue
			String stackIdx = String.valueOf(i);
			jsonItemStack = jsonInventory.get(stackIdx);
			itemStack = MCore.gson.fromJson(jsonItemStack, ItemStack.class);
			itemStacks[i] = itemStack;
		}
		ret.setContents(itemStacks);
		
		return ret;
	}
	
}