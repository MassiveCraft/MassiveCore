package com.massivecraft.massivecore.adapter;

import java.lang.reflect.Type;

import org.bson.types.ObjectId;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ObjectIdAdapter implements JsonDeserializer<ObjectId>, JsonSerializer<ObjectId>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ObjectIdAdapter i = new ObjectIdAdapter();
	public static ObjectIdAdapter get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public JsonElement serialize(ObjectId src, Type typeOfSrc, JsonSerializationContext context)
	{
		return convertObjectIdToJsonPrimitive(src);
	}
	
	@Override
	public ObjectId deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		return convertJsonElementToObjectId(json);
	}
	
	// -------------------------------------------- //
	// STATIC LOGIC
	// -------------------------------------------- //
	
	public static String convertObjectIdToString(ObjectId objectId)
	{
		return objectId.toString();
	}
	
	public static JsonPrimitive convertObjectIdToJsonPrimitive(ObjectId objectId)
	{
		return new JsonPrimitive(convertObjectIdToString(objectId));
	}
	
	// Can return null
	public static ObjectId convertStringToObjectId(String string)
	{
		return ObjectId.massageToObjectId(string);
	}
	
	public static ObjectId convertJsonElementToObjectId(JsonElement jsonElement)
	{
		return convertStringToObjectId(jsonElement.getAsString());
	}
	
}
