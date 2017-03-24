package com.massivecraft.massivecore.adapter;

import com.massivecraft.massivecore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializer;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonNull;
import com.massivecraft.massivecore.xlib.gson.JsonParseException;
import com.massivecraft.massivecore.xlib.gson.JsonPrimitive;
import com.massivecraft.massivecore.xlib.gson.JsonSerializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonSerializer;
import org.bukkit.Sound;

import java.lang.reflect.Type;

public class AdapterSound implements JsonDeserializer<Sound>, JsonSerializer<Sound>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static final AdapterSound i = new AdapterSound();
	public static AdapterSound get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public JsonElement serialize(Sound src, Type typeOfSrc, JsonSerializationContext context)
	{
		if (src == null) return JsonNull.INSTANCE;
		return new JsonPrimitive(src.name());
	}

	@Override
	public Sound deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		if (json == null) return null;
		if (json.equals(JsonNull.INSTANCE)) return null;
		return Sound.valueOf(json.getAsString());
	}

}
