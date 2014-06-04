package com.massivecraft.massivecore.store;

import java.util.Iterator;
import java.util.Map.Entry;

import com.massivecraft.massivecore.xlib.gson.JsonArray;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonNull;
import com.massivecraft.massivecore.xlib.gson.JsonObject;

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
			// TODO: This is actually not safe since JsonPrimitive is mutable.
			// However there is no easy way to clone a JsonPrimitive and I thought they were mutable anyways.
			return element;
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
