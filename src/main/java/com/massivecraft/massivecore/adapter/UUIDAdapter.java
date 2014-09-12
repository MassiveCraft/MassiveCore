package com.massivecraft.massivecore.adapter;

import java.lang.reflect.Type;
import java.util.UUID;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class UUIDAdapter implements JsonDeserializer<UUID>, JsonSerializer<UUID>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static UUIDAdapter i = new UUIDAdapter();
	public static UUIDAdapter get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public JsonElement serialize(UUID src, Type typeOfSrc, JsonSerializationContext context)
	{
		return convertUUIDToJsonPrimitive(src);
	}
	
	@Override
	public UUID deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		return convertJsonElementToUUID(json);
	}
	
	// -------------------------------------------- //
	// STATIC LOGIC
	// -------------------------------------------- //
	
	public static String convertUUIDToString(UUID objectId)
	{
		return objectId.toString();
	}
	
	public static JsonPrimitive convertUUIDToJsonPrimitive(UUID objectId)
	{
		return new JsonPrimitive(convertUUIDToString(objectId));
	}
	
	public static UUID convertStringToUUID(String string)
	{
		return UUID.fromString(string);
	}
	
	public static UUID convertJsonElementToUUID(JsonElement jsonElement)
	{
		return convertStringToUUID(jsonElement.getAsString());
	}
	
}
