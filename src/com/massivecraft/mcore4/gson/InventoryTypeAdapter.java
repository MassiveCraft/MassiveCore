package com.massivecraft.mcore4.gson;

import java.lang.reflect.Type;

import org.bukkit.craftbukkit.inventory.CraftInventoryCustom;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.mcore4.lib.gson.JsonDeserializationContext;
import com.massivecraft.mcore4.lib.gson.JsonDeserializer;
import com.massivecraft.mcore4.lib.gson.JsonElement;
import com.massivecraft.mcore4.lib.gson.JsonObject;
import com.massivecraft.mcore4.lib.gson.JsonParseException;
import com.massivecraft.mcore4.lib.gson.JsonPrimitive;
import com.massivecraft.mcore4.lib.gson.JsonSerializationContext;
import com.massivecraft.mcore4.lib.gson.JsonSerializer;

public class InventoryTypeAdapter implements JsonDeserializer<Inventory>, JsonSerializer<Inventory>
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
		return serialize(src);
	}
	
	@Override
	public Inventory deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		return deserialize(json);
	}
	
	// -------------------------------------------- //
	// STATIC LOGIC
	// -------------------------------------------- //
	
	public static JsonElement serialize(Inventory src)
	{
		JsonObject jsonInventory = new JsonObject();
		ItemStack[] itemStacks = src.getContents();
		jsonInventory.add(SIZE, new JsonPrimitive(itemStacks.length));
		
		for (int i = 0; i < itemStacks.length; i++)
		{
			ItemStack itemStack = itemStacks[i];
			JsonObject jsonItemStack = ItemStackAdapter.serialize(itemStack);
			if (jsonItemStack == null) continue;
			jsonInventory.add(String.valueOf(i), jsonItemStack);
		}
		
		return jsonInventory;
	}
	
	public static Inventory deserialize(JsonElement json)
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
			ItemStack itemStack = ItemStackAdapter.deserialize(jsonItemStack);
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