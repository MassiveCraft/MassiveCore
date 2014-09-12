package com.massivecraft.massivecore.adapter;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

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
