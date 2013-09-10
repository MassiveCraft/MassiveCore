package com.massivecraft.mcore.adapter;

import java.lang.reflect.Type;

import com.massivecraft.mcore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.mcore.xlib.gson.JsonDeserializer;
import com.massivecraft.mcore.xlib.gson.JsonElement;
import com.massivecraft.mcore.xlib.gson.JsonParseException;
import com.massivecraft.mcore.xlib.gson.JsonSerializationContext;
import com.massivecraft.mcore.xlib.gson.JsonSerializer;

public class JsonElementAdapter implements JsonDeserializer<JsonElement>, JsonSerializer<JsonElement>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static JsonElementAdapter i = new JsonElementAdapter();
	public static JsonElementAdapter get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public JsonElement serialize(JsonElement src, Type typeOfSrc, JsonSerializationContext context)
	{
		return src;
	}
	
	@Override
	public JsonElement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		return json;
	}
	
}
