package com.massivecraft.mcore.adapter;

import java.lang.reflect.Type;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.mcore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.mcore.xlib.gson.JsonDeserializer;
import com.massivecraft.mcore.xlib.gson.JsonElement;
import com.massivecraft.mcore.xlib.gson.JsonObject;
import com.massivecraft.mcore.xlib.gson.JsonParseException;
import com.massivecraft.mcore.xlib.gson.JsonSerializationContext;
import com.massivecraft.mcore.xlib.gson.JsonSerializer;

public class ItemStackAdapter implements JsonDeserializer<ItemStack>, JsonSerializer<ItemStack>
{	
	// -------------------------------------------- //
	// IMPLEMENTATION
	// -------------------------------------------- //
	
	@Override
	public JsonElement serialize(ItemStack itemStack, Type typeOfSrc, JsonSerializationContext context)
	{
		// We always use the latest version when we serialize.
		return ItemStackAdapterV2.erialize(itemStack);
	}
	
	@Override
	public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		// We need to decide what version to use when we deserialize.
		JsonDeserializer<ItemStack> deserializer = pickItemStackDeserializer(json);
		return deserializer.deserialize(json, typeOfT, context);	
	}
	
	// -------------------------------------------- //
	// PICK ITEM STACK ADAPTER
	// -------------------------------------------- //
	
	protected static JsonDeserializer<ItemStack> pickItemStackDeserializer(JsonElement jsonElement)
	{
		JsonDeserializer<ItemStack> ret = ItemStackAdapterV2.get();
		
		// Check for "nothing"
		if (jsonElement == null) return ret;
		
		// Must be a JsonObject
		if (jsonElement.isJsonObject() == false) return ret;
		JsonObject json = jsonElement.getAsJsonObject();
		
		// Is it V1?
		if (json.has(ItemStackAdapterV1.TYPE))
		{
			ret = ItemStackAdapterV1.get(); 
		}
		
		return ret;
	}

	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	public static ItemStackAdapter i = new ItemStackAdapter();
	public static ItemStackAdapter get() { return i; }
	
}
