package com.massivecraft.massivecore.adapter;

import java.lang.reflect.Type;
import org.bukkit.inventory.ItemStack;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializer;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonParseException;
import com.massivecraft.massivecore.xlib.gson.JsonSerializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonSerializer;

/**
 * This is a GSON serializer/deserializer for the Bukkit ItemStack. Why not use
 * the built in Bukkit serializer/deserializer? I would have loved to do that :)
 * but sadly that one is YAML centric and cannot be used with json in a good
 * way. This serializer requires manual updating to work but produces clean
 * json. See the file itemstackformat.txt for more info.
 */
public class AdapterItemStack implements JsonDeserializer<ItemStack>, JsonSerializer<ItemStack>
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //

	public static AdapterItemStack i = new AdapterItemStack();
	public static AdapterItemStack get() { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private AdapterItemStackInner inner;
	public AdapterItemStackInner getInner() { return this.inner; }
	public void setInner(AdapterItemStackInner inner) { this.inner = inner; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public AdapterItemStack()
	{
		// 1.9
		try
		{
			this.inner = AdapterItemStackInner19.get();
			return;
		}
		catch (Throwable t)
		{
			
		}
		
		// 1.8
		try
		{
			this.inner = AdapterItemStackInner18.get();
			return;
		}
		catch (Throwable t)
		{
			
		}
		
		// 1.7
		this.inner = AdapterItemStackInner17.get();
	}
	
	// -------------------------------------------- //
	// GSON INTERFACE IMPLEMENTATION
	// -------------------------------------------- //

	@Override
	public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context)
	{
		return this.getInner().erialize(src);
	}

	@Override
	public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		return this.getInner().erialize(json);
	}

}
