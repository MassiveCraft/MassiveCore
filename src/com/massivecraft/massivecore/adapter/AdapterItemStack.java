package com.massivecraft.massivecore.adapter;

import com.massivecraft.massivecore.item.DataItemStack;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializer;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonParseException;
import com.massivecraft.massivecore.xlib.gson.JsonSerializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonSerializer;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

/**
 * This is a GSON serializer/deserializer for the Bukkit ItemStack. Why not use
 * the built in Bukkit serializer/deserializer? I would have loved to do that :)
 * but sadly that one is YAML centric and cannot be used with json in a good
 * way. This serializer requires manual updating to work but produces clean
 * json. See the file itemstackformat.txt for more info.
 */
public class AdapterItemStack implements JsonDeserializer<ItemStack>, JsonSerializer<ItemStack>
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //

	private static final AdapterItemStack i = new AdapterItemStack();
	public static AdapterItemStack get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context)
	{
		DataItemStack dataItemStack = new DataItemStack(src);
		return context.serialize(dataItemStack);
	}

	@Override
	public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		DataItemStack dataItemStack = context.deserialize(json, DataItemStack.class);
		return dataItemStack.toBukkit();
	}

}
