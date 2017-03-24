package com.massivecraft.massivecore.adapter;

import com.massivecraft.massivecore.collections.MassiveListDef;
import com.massivecraft.massivecore.item.DataBannerPattern;
import com.massivecraft.massivecore.xlib.gson.JsonArray;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializer;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonNull;
import com.massivecraft.massivecore.xlib.gson.JsonObject;
import com.massivecraft.massivecore.xlib.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Iterator;

public class AdapterBannerPatterns implements JsonDeserializer<MassiveListDef<DataBannerPattern>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static final AdapterBannerPatterns i = new AdapterBannerPatterns();
	public static AdapterBannerPatterns get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	// Default serialization is fine.
	// We do however provide a smarter deserialiser.
	// This is for the sole purpose of upgrading from the old deprecated database format.
	//
	// In the old version (around version 2.8.10) we made use of a list with primitives.
	// id, color, id, color...
	// They were just coming in that order and were not wrapped in some kind of entry.
	//
	// The data types are the same in the old and new version.
	// String, Number, String, Number...
	//
	// In the new version we do however wrap them in the DataBannerPattern.

	@Override
	public MassiveListDef<DataBannerPattern> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		// Create
		MassiveListDef<DataBannerPattern> ret = new MassiveListDef<>();
		
		// Null (Def implementation is never null)
		if (json == null) return ret;
		if (json.equals(JsonNull.INSTANCE)) return ret;
		
		// It is an array in both old and new version
		JsonArray array = json.getAsJsonArray();
		
		// Empty?
		if (array.size() == 0) return ret;
		
		// First element indicates version
		JsonElement first = array.get(0);
		if (first instanceof JsonObject)
		{
			// New
			for (JsonElement element : array)
			{
				DataBannerPattern dataBannerPattern = context.deserialize(element, DataBannerPattern.class);
				ret.add(dataBannerPattern);
			}
		}
		else
		{
			// Old aka Upgrade Mode
			Iterator<JsonElement> iterator = array.iterator();
			while (iterator.hasNext())
			{
				DataBannerPattern dataBannerPattern = new DataBannerPattern();
				
				JsonElement idElement = iterator.next();
				String id = idElement.getAsString();
				dataBannerPattern.setId(id);
				
				JsonElement colorElement = iterator.next();
				Integer color = colorElement.getAsInt();
				dataBannerPattern.setColor(color);
				
				ret.add(dataBannerPattern);
			}
		}
		
		return ret;
	}

}
