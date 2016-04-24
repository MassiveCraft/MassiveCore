package com.massivecraft.massivecore.adapter;

import java.lang.reflect.Type;

import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializer;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonNull;
import com.massivecraft.massivecore.xlib.gson.JsonParseException;
import com.massivecraft.massivecore.xlib.gson.JsonSerializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonSerializer;

public class AdapterMson implements JsonDeserializer<Mson>, JsonSerializer<Mson>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	public static AdapterMson i = new AdapterMson();
	public static AdapterMson get() { return i; }
	public AdapterMson() {}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public JsonElement serialize(Mson src, Type typeOfSrc, JsonSerializationContext context)
	{
		if (src == null) return JsonNull.INSTANCE;
		return src.toJson();
	}

	@Override
	public Mson deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		return Mson.fromJson(json);
	}

}
