package com.massivecraft.massivecore.adapter;

import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializer;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonParseException;
import com.massivecraft.massivecore.xlib.gson.JsonSerializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonSerializer;

import java.lang.reflect.Type;

public class AdapterMson implements JsonDeserializer<Mson>, JsonSerializer<Mson>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static final AdapterMson i = new AdapterMson();
	public static AdapterMson get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public JsonElement serialize(Mson src, Type typeOfSrc, JsonSerializationContext context)
	{
		return Mson.toJson(src);
	}

	@Override
	public Mson deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		return Mson.fromJson(json);
	}

}
