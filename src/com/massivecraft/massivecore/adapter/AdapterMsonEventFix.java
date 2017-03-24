package com.massivecraft.massivecore.adapter;

import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.mson.MsonEvent;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializer;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonParseException;
import com.massivecraft.massivecore.xlib.gson.JsonSerializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonSerializer;

import java.lang.reflect.Type;

public class AdapterMsonEventFix implements JsonDeserializer<MsonEvent>, JsonSerializer<MsonEvent>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static final AdapterMsonEventFix i = new AdapterMsonEventFix();
	public static AdapterMsonEventFix get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public JsonElement serialize(MsonEvent src, Type typeOfSrc, JsonSerializationContext context)
	{
		return Mson.getGson(false).toJsonTree(src);
	}

	@Override
	public MsonEvent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		MsonEvent ret = Mson.getGson(false).fromJson(json, MsonEvent.class);
		ret.repair();
		return ret;
	}
	
}
