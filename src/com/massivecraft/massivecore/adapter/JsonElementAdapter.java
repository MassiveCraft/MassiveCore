package com.massivecraft.massivecore.adapter;

import java.lang.reflect.Type;

import com.massivecraft.massivecore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializer;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonParseException;
import com.massivecraft.massivecore.xlib.gson.JsonSerializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonSerializer;

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
