package com.massivecraft.mcore4.adapter;

import java.lang.reflect.Type;

import org.bukkit.craftbukkit.inventory.CraftInventoryCustom;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.mcore4.xlib.gson.JsonDeserializationContext;
import com.massivecraft.mcore4.xlib.gson.JsonDeserializer;
import com.massivecraft.mcore4.xlib.gson.JsonElement;
import com.massivecraft.mcore4.xlib.gson.JsonObject;
import com.massivecraft.mcore4.xlib.gson.JsonParseException;
import com.massivecraft.mcore4.xlib.gson.JsonPrimitive;
import com.massivecraft.mcore4.xlib.gson.JsonSerializationContext;
import com.massivecraft.mcore4.xlib.gson.JsonSerializer;

public class InventoryAdapter implements JsonDeserializer<Inventory>, JsonSerializer<Inventory>
{
	// -------------------------------------------- //
	// FIELD NAME CONSTANTS
	// -------------------------------------------- //
	
	public static final String SIZE = "size";

	// -------------------------------------------- //
	// IMPLEMENTATION
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
	// JSON
	// -------------------------------------------- //
	
	public static JsonElement toJson(Inventory src)
	{
		JsonObject jsonInventory = new JsonObject();
		ItemStack[] itemStacks = src.getContents();
		jsonInventory.add(SIZE, new JsonPrimitive(itemStacks.length));
		
		for (int i = 0; i < itemStacks.length; i++)
		{
			ItemStack itemStack = itemStacks[i];
			JsonObject jsonItemStack = ItemStackAdapter.toJson(itemStack);
			if (jsonItemStack == null) continue;
			jsonInventory.add(String.valueOf(i), jsonItemStack);
		}
		
		return jsonInventory;
	}
	
	public static Inventory fromJson(JsonElement json)
	{
		if ( ! json.isJsonObject()) return null;
		JsonObject jsonInventory = json.getAsJsonObject();
		
		if ( ! jsonInventory.has(SIZE)) return null;
		int size = jsonInventory.get(SIZE).getAsInt();
		
		ItemStack[] itemStacks = new ItemStack[size];
		
		for (int i = 0; i < size; i++)
		{
			// Fetch the jsonItemStack or mark it as empty and continue
			String stackIdx = String.valueOf(i);
			JsonElement jsonItemStack = jsonInventory.get(stackIdx);
			ItemStack itemStack = ItemStackAdapter.fromJson(jsonItemStack);
			itemStacks[i] = itemStack;
		}
		
		Inventory ret = new CraftInventoryCustom(null, size, "items");
		ret.setContents(itemStacks);
		return ret;
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	// This utility is nice to have in many cases :)
	public static boolean isInventoryEmpty(Inventory inv)
	{
		if (inv == null) return true;
		for (ItemStack stack : inv.getContents())
		{
			if (stack == null) continue;
			if (stack.getAmount() == 0) continue;
			if (stack.getTypeId() == 0) continue;
			return false;
		}
		return true;
	}
}