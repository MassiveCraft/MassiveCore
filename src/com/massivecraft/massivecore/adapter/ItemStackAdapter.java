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
public class ItemStackAdapter implements JsonDeserializer<ItemStack>, JsonSerializer<ItemStack>
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //

	public static ItemStackAdapter i = new ItemStackAdapter();
	public static ItemStackAdapter get() { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private ItemStackAdapterInner inner;
	public ItemStackAdapterInner getInner() { return this.inner; }
	public void setInner(ItemStackAdapterInner inner) { this.inner = inner; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ItemStackAdapter()
	{
		// 1.9
		try
		{
			this.inner = ItemStackAdapterInner19.get();
			return;
		}
		catch (Throwable t)
		{
			
		}
		
		// 1.8
		try
		{
			this.inner = ItemStackAdapterInner18.get();
			return;
		}
		catch (Throwable t)
		{
			
		}
		
		// 1.7
		this.inner = ItemStackAdapterInner17.get();
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
