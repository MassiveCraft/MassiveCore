package com.massivecraft.massivecore.adapter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

import com.massivecraft.massivecore.collections.BackstringEnumSet;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializer;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonParseException;
import com.massivecraft.massivecore.xlib.gson.JsonSerializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonSerializer;
import com.massivecraft.massivecore.xlib.gson.reflect.TypeToken;

public class BackstringEnumSetAdapter implements JsonDeserializer<BackstringEnumSet<?>>, JsonSerializer<BackstringEnumSet<?>>
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public final static Type stringSetType = new TypeToken<Set<String>>(){}.getType(); 
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static BackstringEnumSetAdapter i = new BackstringEnumSetAdapter();
	public static BackstringEnumSetAdapter get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public JsonElement serialize(BackstringEnumSet<?> src, Type type, JsonSerializationContext context)
	{
		return context.serialize(src.getStringSet(), stringSetType);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BackstringEnumSet<?> deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
	{
		Set<String> stringSet = context.deserialize(json, stringSetType);
		
		ParameterizedType ptype = (ParameterizedType) type;
		Type[] args = ptype.getActualTypeArguments();
		Class<?> clazz = (Class<?>) args[0];
		
		return new BackstringEnumSet(clazz, stringSet);
	}

}
