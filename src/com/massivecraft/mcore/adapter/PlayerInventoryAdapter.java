package com.massivecraft.mcore.adapter;

import java.lang.reflect.Type;

import org.bukkit.inventory.PlayerInventory;

import com.massivecraft.mcore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.mcore.xlib.gson.JsonDeserializer;
import com.massivecraft.mcore.xlib.gson.JsonElement;
import com.massivecraft.mcore.xlib.gson.JsonParseException;
import com.massivecraft.mcore.xlib.gson.JsonSerializationContext;
import com.massivecraft.mcore.xlib.gson.JsonSerializer;

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