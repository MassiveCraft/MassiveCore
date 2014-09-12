package com.massivecraft.massivecore.adapter;

import java.lang.reflect.Type;

import org.bukkit.inventory.PlayerInventory;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class PlayerInventoryAdapter implements JsonDeserializer<PlayerInventory>, JsonSerializer<PlayerInventory>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static PlayerInventoryAdapter i = new PlayerInventoryAdapter();
	public static PlayerInventoryAdapter get() { return i; }

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public JsonElement serialize(PlayerInventory src, Type typeOfSrc, JsonSerializationContext context)
	{
		return InventoryAdapter.toJson(src);
	}
	
	@Override
	public PlayerInventory deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		return (PlayerInventory) InventoryAdapter.fromJson(json);
	}
	
}
