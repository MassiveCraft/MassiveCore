package com.massivecraft.mcore5.adapter;

import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagByte;
import net.minecraft.server.NBTTagByteArray;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagDouble;
import net.minecraft.server.NBTTagEnd;
import net.minecraft.server.NBTTagFloat;
import net.minecraft.server.NBTTagInt;
import net.minecraft.server.NBTTagIntArray;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.NBTTagLong;
import net.minecraft.server.NBTTagShort;
import net.minecraft.server.NBTTagString;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

import com.massivecraft.mcore5.xlib.gson.JsonArray;
import com.massivecraft.mcore5.xlib.gson.JsonElement;
import com.massivecraft.mcore5.xlib.gson.JsonObject;
import com.massivecraft.mcore5.xlib.gson.JsonPrimitive;

public class NbtGsonConverter
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public final static String TYPE = "type";
	public final static String ELEMTYPE = "elemtype";
	public final static String VAL = "val";
	public final static String NAME = "name";
	
	// -------------------------------------------- //
	// GSON TO NBT
	// -------------------------------------------- //
	
	public static NBTBase gsonToNbt(JsonElement jsonElement)
	{
		return gsonToNbt(jsonElement, null);
	}
	
	public static NBTBase gsonToNbt(JsonElement jsonElement, String name)
	{
		// Verify and cast the jsonElement into a jsonObject.
		// We could have used jsonObject as parameter type but this method signature is more flexible.
		if (!jsonElement.isJsonObject())
		{
			// must be a json object
			return null;
		}
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		
		// Use the internal name if there is one
		JsonElement nameElement = jsonObject.get(NAME);
		if (nameElement != null)
		{
			name = nameElement.getAsString();
		}
		
		// Fetch the type-info
		JsonElement typeElement = jsonObject.get(TYPE);
		if (typeElement == null)
		{
			// must have a type
			return null;
		}
		NBType type = NBType.getByName(typeElement.getAsString());
		
		// Fetch the elemtype-info (used by NBTTagList only)
		NBType elemtype = NBType.UNKNOWN;
		if (type == NBType.LIST)
		{
			JsonElement elemtypeElement = jsonObject.get(ELEMTYPE);
			if (elemtypeElement == null)
			{
				// must have an elemtype
				return null;
			}
			elemtype = NBType.getByName(elemtypeElement.getAsString());
		}
		
		// Fetch the value field
		JsonElement val = jsonObject.get(VAL);
		
		// Convert the value based on the info we gathered
		return gsonValToNbt(val, name, type, elemtype);
	}
	
	public static NBTBase gsonValToNbt(JsonElement val, String name, NBType type, NBType elemtype)
	{
		NBTBase ret = null;
		
		switch (type)
		{
			case END:
				ret = new NBTTagEnd();
			break;
			
			case BYTE:
				ret = new NBTTagByte(name, val.getAsByte());
			break;
			
			case SHORT:
				ret = new NBTTagShort(name, val.getAsShort());
			break;

			case INT:
				ret = new NBTTagInt(name, val.getAsInt());
			break;

			case LONG:
				ret = new NBTTagLong(name, val.getAsLong());
			break;
			
			case FLOAT:
				ret = new NBTTagFloat(name, val.getAsFloat());
			break;
			
			case DOUBLE:
				ret = new NBTTagDouble(name, val.getAsDouble());
			break;
			
			case BYTEARRAY:
				JsonArray jsonBytes = val.getAsJsonArray();
				int jsonBytesSize = jsonBytes.size();
				byte[] byteArray = new byte[jsonBytesSize];
				for (int index = 0 ; index < jsonBytesSize ; index++)
				{
					byte b = jsonBytes.get(index).getAsByte();
					byteArray[index] = b;
				}
				ret = new NBTTagByteArray(name, byteArray);
			break;
			
			case INTARRAY:
				JsonArray jsonInts = val.getAsJsonArray();
				int jsonIntsSize = jsonInts.size();
				int[] intArray = new int[jsonIntsSize];
				for (int index = 0 ; index < jsonIntsSize ; index++)
				{
					int i = jsonInts.get(index).getAsInt();
					intArray[index] = i;
				}
				ret = new NBTTagIntArray(name, intArray);
			break;
			
			case STRING:
				ret = new NBTTagString(name, val.getAsString());
			break;
			
			case LIST:
				NBTTagList nbtlist = new NBTTagList(name);
				
				if (!val.isJsonArray())
				{
					// must be an array
					return null;
				}
				
				Iterator<JsonElement> iter = val.getAsJsonArray().iterator();
				while (iter.hasNext())
				{
					nbtlist.add(gsonValToNbt(iter.next(), null, elemtype, NBType.UNKNOWN));
				}
				ret = nbtlist;
			break;
			
			case COMPOUND:
				NBTTagCompound compound = new NBTTagCompound(name);
				
				if (!val.isJsonObject())
				{
					// must be an object
					return null;
				}
				
				JsonObject jsonCompound = val.getAsJsonObject();
				
				for(Entry<String, JsonElement> entry : jsonCompound.entrySet())
				{
					String childName = entry.getKey();
					JsonElement childJson = entry.getValue();
					NBTBase child = gsonToNbt(childJson, childName);
					if (child == null) continue;
					compound.set(childName, child);
				}
				
				ret = compound;
			break;
		}
		
		return ret;
	}
	
	// -------------------------------------------- //
	// NBT TO GSON
	// -------------------------------------------- //
	
	public static JsonObject nbtToGson(NBTBase nbt, boolean includeName)
	{
		JsonObject ret = new JsonObject();
		
		String name = nbt.getName();
		if (includeName && name != null)
		{
			ret.addProperty(NAME, name);
		}
		
		NBType type = NBType.getById(nbt.getTypeId());
		ret.addProperty(TYPE, type.getName());
		
		if (type == NBType.LIST)
		{
			ret.addProperty(ELEMTYPE, NBType.getByTag(((NBTTagList)nbt).get(0)).getName());
		}
		
		JsonElement val = nbtToGsonVal(nbt);
		if (val == null)
		{
			return null;
		}
		ret.add(VAL, val);
		
		return ret;
	}
	
	public static JsonElement nbtToGsonVal(NBTBase nbt)
	{
		JsonElement val = null;
		
		NBType type = NBType.getByTag(nbt);
		
		switch (type)
		{
			case END:
				// this should never happen
			break;
			
			case BYTE:
				val = new JsonPrimitive(((NBTTagByte) nbt).data);
			break;
			
			case SHORT:
				val = new JsonPrimitive(((NBTTagShort) nbt).data);
			break;
			
			case INT:
				val = new JsonPrimitive(((NBTTagInt) nbt).data);
			break;
			
			case LONG:
				val = new JsonPrimitive(((NBTTagLong) nbt).data);
			break;
			
			case FLOAT:
				val = new JsonPrimitive(((NBTTagFloat) nbt).data);
			break;
			
			case DOUBLE:
				val = new JsonPrimitive(((NBTTagDouble) nbt).data);
			break;
			
			case BYTEARRAY:
				JsonArray jsonBytes = new JsonArray();
				for (byte elem : ((NBTTagByteArray) nbt).data)
				{
					jsonBytes.add(new JsonPrimitive(elem));
				}
				val = jsonBytes;
			break;
			
			case INTARRAY:
				JsonArray jsonInts = new JsonArray();
				for (int elem : ((NBTTagIntArray) nbt).data)
				{
					jsonInts.add(new JsonPrimitive(elem));
				}
				val = jsonInts;
			break;
			
			case STRING:
				val = new JsonPrimitive(((NBTTagString) nbt).data);
			break;
			
			case LIST:
				NBTTagList nbtlist = (NBTTagList)nbt;
				int size = nbtlist.size();
				if (size <= 0)
				{
					// NBTTagList may not be empty
					return null;
				}
				
				JsonArray jsonElems = new JsonArray();
				for (int i = 0 ; i < size ; i++)
				{
					jsonElems.add(nbtToGsonVal(nbtlist.get(i)));
				}
				val = jsonElems;
			break;
			
			case COMPOUND:
				JsonObject jsonObject = new JsonObject();
				for (NBTBase child : getCompoundChildren((NBTTagCompound)nbt))
				{
					jsonObject.add(child.getName(), nbtToGson(child, false));
				}
				val = jsonObject;
			break;
		}
		
		return val;
	}

	// -------------------------------------------- //
	// UTILS
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	public static Collection<NBTBase> getCompoundChildren(NBTTagCompound compound)
	{
		return (Collection<NBTBase>)compound.c();
	}
}