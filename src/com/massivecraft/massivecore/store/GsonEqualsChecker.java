package com.massivecraft.massivecore.store;

import com.massivecraft.massivecore.xlib.gson.JsonArray;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonNull;
import com.massivecraft.massivecore.xlib.gson.JsonObject;
import com.massivecraft.massivecore.xlib.gson.JsonPrimitive;
import com.massivecraft.massivecore.xlib.gson.internal.LazilyParsedNumber;
import org.apache.commons.lang.StringUtils;

import java.util.Map.Entry;

public class GsonEqualsChecker
{
	// The argument one must be JsonElement, and can not be null.
	// The argument twoObject may be anything, even null.
	public static boolean equals(JsonElement one, Object twoObject)
	{
		// Null check (one can't ever be null)
		if (twoObject == null) return false;
		
		// Object identity speedup
		if (one == twoObject) return true;
		
		// Type-Switch
		if (one.isJsonObject())
		{
			// JsonObject
			return objectEquals((JsonObject)one, twoObject);
		}
		else if (one.isJsonArray())
		{
			// JsonArray
			return arrayEquals((JsonArray)one, twoObject);
		}
		else if (one.isJsonPrimitive())
		{
			// JsonPrimitive
			return primitiveEquals((JsonPrimitive)one, twoObject);
		}
		else if (one.isJsonNull())
		{
			// JsonNull
			return nullEquals((JsonNull)one, twoObject);
		}
		else
		{
			// ???
			throw new IllegalArgumentException("Unsupported value type for: " + one);
		}
	}

	// The argument one must be JsonObject, and can not be null.
	// The argument twoObject may be anything, even null.
	public static boolean objectEquals(JsonObject one, Object twoObject)
	{
		// Null check (one can't ever be null)
		if (twoObject == null) return false;
		
		// Object identity speedup
		if (one == twoObject) return true;
		
		// twoObject must be JsonObject
		if (!(twoObject instanceof JsonObject)) return false;
		
		// Cast to JsonObject
		JsonObject two = (JsonObject)twoObject;
		
		// Size must be the same
		if (one.entrySet().size() != two.entrySet().size()) return false;
		
		// And each entry must exist and be the same
		for (Entry<String, JsonElement> entry : one.entrySet())
		{
			if (!equals(entry.getValue(), two.get(entry.getKey()))) return false;
		}
		return true;
	}
	
	// The argument one must be JsonArray, and can not be null.
	// The argument twoObject may be anything, even null.
	public static boolean arrayEquals(JsonArray one, Object twoObject)
	{
		// Null check (one can't ever be null)
		if (twoObject == null) return false;
		
		// Object identity speedup
		if (one == twoObject) return true;
		
		// twoObject must be JsonArray
		if (!(twoObject instanceof JsonArray)) return false;
		
		// Cast to JsonArray
		JsonArray two = (JsonArray)twoObject;
		
		// Size must be the same
		int size = one.size();
		if (two.size() != size) return false;
		
		// And each element index must be the same
		for (int i = 0; i < size ; i++)
		{
			if (!equals(one.get(i), two.get(i))) return false;
		}
		return true;
	}
	
	// The argument one must be JsonPrimitive, and can not be null.
	// The argument twoObject may be anything, even null.
	public static boolean primitiveEquals(JsonPrimitive one, Object twoObject)
	{
		// Null check (one can't ever be null)
		if (twoObject == null) return false;
		
		// Object identity speedup
		if (one == twoObject) return true;
		
		// if twoObject is JsonObject or JsonArray we are not equal.
		if (!(twoObject instanceof JsonPrimitive)) return false;
		
		// Cast to JsonPrimitive
		JsonPrimitive two = (JsonPrimitive)twoObject;
		
		// Boolean check
		if (one.isBoolean())
		{
			if (!two.isBoolean()) return false;
			return one.getAsBoolean() == two.getAsBoolean();
		}
		
		// Number check
		if (one.isNumber())
		{
			if (!two.isNumber()) return false;
			Number oneNumber = one.getAsNumber();
			Number twoNumber = two.getAsNumber();
			
			boolean floating = isFloating(oneNumber);
			if (floating)
			{
				// Our epsilon is pretty big in order to see float and double as the same.
				return Math.abs(oneNumber.doubleValue() - twoNumber.doubleValue()) < 0.0001D;
			}
			else
			{
				return oneNumber.longValue() == twoNumber.longValue(); 
			}
			
		}
		
		// String check
		if (one.isString())
		{
			if (!two.isString()) return false;
			return one.getAsString().equals(two.getAsString());
		}
		
		throw new IllegalArgumentException("Unsupported value type for: " + one);
	}
	
	// The argument one must be JsonNull, and can not be null.
	// The argument twoObject may be anything, even null.
	public static boolean nullEquals(JsonNull one, Object twoObject)
	{
		// Null check (one can't ever be null)
		if (twoObject == null) return false;
		
		return twoObject == JsonNull.INSTANCE;
	}
	
	public static boolean isFloating(Number number)
	{
		if (number instanceof LazilyParsedNumber)
		{
			return StringUtils.contains(number.toString(), '.');
		}
		return (number instanceof Double || number instanceof Float);
	}
}
