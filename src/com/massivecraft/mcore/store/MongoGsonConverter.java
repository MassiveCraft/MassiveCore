package com.massivecraft.mcore.store;

import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.massivecraft.mcore.xlib.bson.types.ObjectId;
import com.massivecraft.mcore.xlib.gson.JsonArray;
import com.massivecraft.mcore.xlib.gson.JsonElement;
import com.massivecraft.mcore.xlib.gson.JsonNull;
import com.massivecraft.mcore.xlib.gson.JsonObject;
import com.massivecraft.mcore.xlib.gson.JsonPrimitive;
import com.massivecraft.mcore.xlib.mongodb.BasicDBList;
import com.massivecraft.mcore.xlib.mongodb.BasicDBObject;
import com.massivecraft.mcore.xlib.mongodb.DBObject;

public final class MongoGsonConverter
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final String DOT_NORMAL = ".";
	public static final String DOT_MONGO = "<dot>";
	
	public static final String DOLLAR_NORMAL = "$";
	public static final String DOLLAR_MONGO = "<dollar>";
	
	// -------------------------------------------- //
	// GSON 2 MONGO
	// -------------------------------------------- //
	
	public static String gson2MongoKey(String key)
	{
		key = StringUtils.replace(key, DOT_NORMAL, DOT_MONGO);
		key = StringUtils.replace(key, DOLLAR_NORMAL, DOLLAR_MONGO);
		return key;
	}
	
	public static BasicDBObject gson2MongoObject(JsonElement inElement)
	{
		JsonObject in = inElement.getAsJsonObject();
		BasicDBObject out = new BasicDBObject();
		for (Entry<String, JsonElement> entry : in.entrySet())
		{
			String key = gson2MongoKey(entry.getKey());
			JsonElement val = entry.getValue();
			if (val.isJsonArray())
			{
				out.put(key, gson2MongoArray(val));
			}
			else if (val.isJsonObject())
			{
				out.put(key, gson2MongoObject(val));
			}
			else
			{
				out.put(key, gson2MongoPrimitive(val));
			}
		}
		return out;
	}
	
	public static BasicDBList gson2MongoArray(JsonElement inElement)
	{
		JsonArray in = inElement.getAsJsonArray();
		BasicDBList out = new BasicDBList();
		for (int i = 0; i < in.size(); i++)
		{
			JsonElement element = in.get(i);
			if (element.isJsonArray())
			{
				out.add(gson2MongoArray(element));
			}
			else if (element.isJsonObject())
			{
				out.add(gson2MongoObject(element));
			}
			else
			{ 
				out.add(gson2MongoPrimitive(element));
			}
		}
		return out;
	}
	
	public static Object gson2MongoPrimitive(JsonElement inElement)
	{
		if (inElement.isJsonNull()) return null;
		JsonPrimitive in = inElement.getAsJsonPrimitive();
		if (in.isBoolean()) return in.getAsBoolean();
		if (in.isNumber()) return in.getAsNumber().doubleValue();
		if (in.isString()) return in.getAsString();
		throw new IllegalArgumentException("Unsupported value type for: " + in);
	}
	
	// -------------------------------------------- //
	// MONGO 2 GSON
	// -------------------------------------------- //
	
	public static String mongo2GsonKey(String key)
	{
		key = StringUtils.replace(key, DOT_MONGO, DOT_NORMAL);
		key = StringUtils.replace(key, DOLLAR_MONGO, DOLLAR_NORMAL);
		return key;
	}
	
	public static JsonObject mongo2GsonObject(DBObject inObject)
	{
		if (!(inObject instanceof BasicDBObject)) throw new IllegalArgumentException("Expected BasicDBObject as argument type!");
		BasicDBObject in = (BasicDBObject)inObject;
		
		JsonObject jsonObject = new JsonObject();
		for (Entry<String, Object> entry : in.entrySet())
		{
			String key = mongo2GsonKey(entry.getKey());
			Object val = entry.getValue();
			if (val instanceof BasicDBList)
			{
				jsonObject.add(key, mongo2GsonArray((BasicDBList)val));
			}
			else if (val instanceof BasicDBObject)
			{
				jsonObject.add(key, mongo2GsonObject((BasicDBObject)val));
			}
			else
			{
				jsonObject.add(key, mongo2GsonPrimitive(val));
			}
		}
		return jsonObject;
	}
	
	public static JsonArray mongo2GsonArray(DBObject inObject)
	{
		if (!(inObject instanceof BasicDBList)) throw new IllegalArgumentException("Expected BasicDBList as argument type!");
		BasicDBList in = (BasicDBList)inObject;
		JsonArray jsonArray = new JsonArray();
		for (int i = 0; i < in.size(); i++)
		{
			Object object = in.get(i);
			if (object instanceof BasicDBList)
			{
				jsonArray.add(mongo2GsonArray((BasicDBList) object));
			}
			else if (object instanceof BasicDBObject)
			{
				jsonArray.add(mongo2GsonObject((BasicDBObject) object));
			}
			else
			{ 
				jsonArray.add(mongo2GsonPrimitive(object));
			}
		}
		return jsonArray;
	}

	public static JsonElement mongo2GsonPrimitive(Object inObject)
	{
		if (inObject == null) return JsonNull.INSTANCE;
		if (inObject instanceof Boolean) return new JsonPrimitive((Boolean) inObject);
		if (inObject instanceof Number) return new JsonPrimitive((Number) inObject);
		if (inObject instanceof String) return new JsonPrimitive((String) inObject);
		if (inObject instanceof Character) return new JsonPrimitive((Character) inObject);
		if (inObject instanceof ObjectId) return new JsonPrimitive(inObject.toString());
		throw new IllegalArgumentException("Unsupported value type for: " + inObject);
	}
}
