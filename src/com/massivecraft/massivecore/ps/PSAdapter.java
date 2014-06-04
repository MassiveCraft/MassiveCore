package com.massivecraft.massivecore.ps;

import java.lang.reflect.Type;

import com.massivecraft.massivecore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializer;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonParseException;

public class PSAdapter implements JsonDeserializer<PS>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static PSAdapter i = new PSAdapter();
	public static PSAdapter get()	{ return i; }
	private PSAdapter() {}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public PS deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		return deserialize(json);
	}
	
	// -------------------------------------------- //
	// STATIC LOGIC
	// -------------------------------------------- //
	
	public static PS deserialize(JsonElement json)
	{
		return PS.valueOf(json);
	}
	
}
