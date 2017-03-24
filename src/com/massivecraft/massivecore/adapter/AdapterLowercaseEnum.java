package com.massivecraft.massivecore.adapter;

import com.massivecraft.massivecore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializer;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonNull;
import com.massivecraft.massivecore.xlib.gson.JsonParseException;
import com.massivecraft.massivecore.xlib.gson.JsonPrimitive;
import com.massivecraft.massivecore.xlib.gson.JsonSerializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonSerializer;

import java.lang.reflect.Type;

public class AdapterLowercaseEnum<T extends Enum<T>> implements JsonDeserializer<T>, JsonSerializer<T>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final Class<T> clazz;
	public Class<T> getClazz() { return this.clazz; }
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	public static <T extends Enum<T>> AdapterLowercaseEnum<T> get(Class<T> clazz)
	{
		return new AdapterLowercaseEnum<>(clazz);
	}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public AdapterLowercaseEnum(Class<T> clazz)
	{
		if (clazz == null) throw new IllegalArgumentException("clazz is null");
		if ( ! clazz.isEnum()) throw new IllegalArgumentException("clazz is not enum");
		this.clazz = clazz;
	}
			
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context)
	{
		// Null
		if (src == null) return JsonNull.INSTANCE;
		
		String comparable = this.getComparable(src);
		return new JsonPrimitive(comparable);
	}

	@Override
	public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		// Null
		if (json == null) return null;
		if (json.equals(JsonNull.INSTANCE)) return null;
		
		T ret = this.getEnumValueFrom(json);
		return ret;
	}

	// -------------------------------------------- //
	// GET ENUM VALUE FROM
	// -------------------------------------------- //
	
	public T getEnumValueFrom(JsonElement json)
	{
		String string = this.getComparable(json);
		return this.getEnumValueFrom(string);
	}
	
	public T getEnumValueFrom(String string)
	{
		string = this.getComparable(string);
		for (T value : this.getEnumValues())
		{
			String comparable = this.getComparable(value);
			if (comparable.equals(string)) return value;
		}
		return null;
	}
	
	// -------------------------------------------- //
	// GET ENUM VALUES
	// -------------------------------------------- //
	
	public T[] getEnumValues()
	{
		Class<T> clazz = this.getClazz();
		T[] ret = clazz.getEnumConstants();
		return ret;
	}
	
	// -------------------------------------------- //
	// GET COMPARABLE
	// -------------------------------------------- //
	
	public String getComparable(Enum<?> value)
	{
		if (value == null) return null;
		return this.getComparable(value.name());
	}
	
	public String getComparable(JsonElement json)
	{
		if (json == null) return null;
		return this.getComparable(json.getAsString());
	}
	
	public String getComparable(String string)
	{
		if (string == null) return null;
		return string.toLowerCase();
	}
	
}
