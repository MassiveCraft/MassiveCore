package com.massivecraft.massivecore.adapter;

import java.lang.reflect.Type;

import org.bukkit.inventory.PlayerInventory;

import com.massivecraft.massivecore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializer;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonParseException;
import com.massivecraft.massivecore.xlib.gson.JsonSerializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonSerializer;

public class AdapterPlayerInventory implements JsonDeserializer<PlayerInventory>, JsonSerializer<PlayerInventory>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static AdapterPlayerInventory i = new AdapterPlayerInventory();
	public static AdapterPlayerInventory get() { return i; }

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public JsonElement serialize(PlayerInventory src, Type typeOfSrc, JsonSerializationContext context)
	{
		return AdapterInventory.toJson(src);
	}
	
	@Override
	public PlayerInventory deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		return (PlayerInventory) AdapterInventory.fromJson(json);
	}
	
}
