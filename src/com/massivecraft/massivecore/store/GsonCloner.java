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
	public static JsonElement clone(JsonElement element)
	{
		// null
		if (element == null) return null;
		
		// JsonNull
		if (element.isJsonNull()) return JsonNull.INSTANCE;
		
		// JsonPrimitive
		if (element.isJsonPrimitive())
		{
			JsonPrimitive primitive = element.getAsJsonPrimitive();
			if (primitive.isBoolean()) return new JsonPrimitive(primitive.getAsBoolean());
			if (primitive.isString()) return new JsonPrimitive(primitive.getAsString());
			if (primitive.isNumber()) return new JsonPrimitive(primitive.getAsNumber());
			
			throw new UnsupportedOperationException("The json primitive: " + primitive + " was not a boolean, number or string");
		}
		
		// JsonObject
		if (element.isJsonObject())
		{
			JsonObject ret = new JsonObject();
			for (Entry<String, JsonElement> entry : ((JsonObject)element).entrySet())
			{
				ret.add(entry.getKey(), clone(entry.getValue()));
			}
			return ret;
		}
		
		// JsonArray
		if (element.isJsonArray())
		{
			JsonArray ret = new JsonArray();
			Iterator<JsonElement> iter = ((JsonArray)element).iterator();
			while (iter.hasNext())
			{
				ret.add(clone(iter.next()));
			}
			return ret;
		}
		
		throw new RuntimeException("Unknown JsonElement class: " + element.getClass().getName());
	}
}
