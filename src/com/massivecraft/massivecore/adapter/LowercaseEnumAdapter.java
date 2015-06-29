package com.massivecraft.massivecore.adapter;

import java.lang.reflect.Type;

import com.massivecraft.massivecore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializer;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonNull;
import com.massivecraft.massivecore.xlib.gson.JsonParseException;
import com.massivecraft.massivecore.xlib.gson.JsonPrimitive;
import com.massivecraft.massivecore.xlib.gson.JsonSerializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonSerializer;

public class LowercaseEnumAdapter<T extends Enum<T>> implements JsonDeserializer<T>, JsonSerializer<T>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected final Class<T> clazz;
	public Class<T> getClazz() { return this.clazz; }
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static <T extends Enum<T>> LowercaseEnumAdapter<T> get(Class<T> clazz)
	{
		return new LowercaseEnumAdapter<T>(clazz);
	}
	
	public LowercaseEnumAdapter(Class<T> clazz)
	{
		if (clazz == null) throw new IllegalArgumentException("passed clazz param is null");
		if ( ! clazz.isEnum()) throw new IllegalArgumentException("passed clazz param must be an enum");
		this.clazz = clazz;
	}
			
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context)
	{
		if (src == null) return JsonNull.INSTANCE;
		return new JsonPrimitive(src.name().toLowerCase());
	}

	@Override
	public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		if (json == null) return null;
		T value = getEnumValueFrom(json);
		return value;
	}

	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public T getEnumValueFrom(JsonElement json)
	{
		return getEnumValueFrom(json.toString());
	}
	
	public T getEnumValueFrom(String str)
	{
		return getEnumValueFrom(str, clazz);
	}
	
	public static <T> T[] getEnumValues(Class<T> clazz)
	{
		if (clazz == null) throw new IllegalArgumentException("passed clazz param is null");
		if ( ! clazz.isEnum()) throw new IllegalArgumentException("passed clazz param must be an enum");
		
		T[] ret = clazz.getEnumConstants();
		if (ret == null) throw new RuntimeException("failed to retrieve enum constants at runtime");
		
		return ret;
	}
	
	public static String getComparable(Enum<?> value)
	{
		if (value == null) return null;
		return getComparable(value.name());
	}
	
	public static String getComparable(String string)
	{
		if (string == null) return null;
		return string.toLowerCase();
	}
	
	public static<T extends Enum<T>> T getEnumValueFrom(String str, Class<T> clazz)
	{
		str = getComparable(str);
		
		for (T value : getEnumValues(clazz))
		{
			if (getComparable(value).equals(str)) return value;
		}
		
		return null;
	}
	
}
