package com.massivecraft.mcore.ps;

import java.lang.reflect.Type;

import com.massivecraft.mcore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.mcore.xlib.gson.JsonDeserializer;
import com.massivecraft.mcore.xlib.gson.JsonElement;
import com.massivecraft.mcore.xlib.gson.JsonParseException;

public class PS2Adapter implements JsonDeserializer<PS2>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static PS2Adapter i = new PS2Adapter();
	public static PS2Adapter get()	{ return i; }
	private PS2Adapter() {}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public PS2 deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		return deserialize(json);
	}
	
	// -------------------------------------------- //
	// STATIC LOGIC
	// -------------------------------------------- //
	
	public static PS2 deserialize(JsonElement json)
	{
		return PS2.valueOf(json);
	}
	
}
