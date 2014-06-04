package com.massivecraft.massivecore.adapter;

import java.lang.reflect.Type;

import org.bukkit.inventory.PlayerInventory;

import com.massivecraft.massivecore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializer;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonParseException;
import com.massivecraft.massivecore.xlib.gson.JsonSerializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonSerializer;

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
