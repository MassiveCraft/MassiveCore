package com.massivecraft.massivecore.store;

import java.util.Iterator;
import java.util.Map.Entry;

import com.massivecraft.massivecore.xlib.gson.JsonArray;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonNull;
import com.massivecraft.massivecore.xlib.gson.JsonObject;
import com.massivecraft.massivecore.xlib.gson.JsonPrimitive;

public class GsonCloner
{
	// All
	public static JsonElement clone(JsonElement element)
	{
		// null
		if (element == null) return null;
		
		// JsonNull
		if (element.isJsonNull()) return JsonNull.INSTANCE;
		
		// JsonPrimitive
		if (element.isJsonPrimitive()) return cloneJsonPrimitive(element.getAsJsonPrimitive());
		
		// JsonObject
		if (element.isJsonObject()) return cloneJsonObject(element.getAsJsonObject());
		
		// JsonArray
		if (element.isJsonArray()) return cloneJsonArray(element.getAsJsonArray());
		
		// Unknown
		throw new RuntimeException("Unknown JsonElement class: " + element.getClass().getName());
	}
	
	// Primitive
	public static JsonPrimitive cloneJsonPrimitive(JsonPrimitive primitive)
	{
		if (primitive.isBoolean()) return new JsonPrimitive(primitive.getAsBoolean());
		if (primitive.isString()) return new JsonPrimitive(primitive.getAsString());
		if (primitive.isNumber()) return new JsonPrimitive(primitive.getAsNumber());
		
		throw new UnsupportedOperationException("The json primitive: " + primitive + " was not a boolean, number or string");
	}
	
	// Object
	public static JsonObject cloneJsonObject(JsonObject object)
	{
		JsonObject ret = new JsonObject();
		for (Entry<String, JsonElement> entry : object.entrySet())
		{
			ret.add(entry.getKey(), clone(entry.getValue()));
		}
		return ret;
	}
	
	// Array
	public static JsonArray cloneJsonArray(JsonArray array)
	{
		JsonArray ret = new JsonArray();
		for (Iterator<JsonElement> iter = array.iterator(); iter.hasNext();)
		{
			ret.add(clone(iter.next()));
		}
		return ret;
	}
	
}
