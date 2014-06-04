package com.massivecraft.massivecore.adapter;

import java.lang.reflect.Type;

import com.massivecraft.massivecore.xlib.bson.types.ObjectId;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializer;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonParseException;
import com.massivecraft.massivecore.xlib.gson.JsonPrimitive;
import com.massivecraft.massivecore.xlib.gson.JsonSerializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonSerializer;

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
