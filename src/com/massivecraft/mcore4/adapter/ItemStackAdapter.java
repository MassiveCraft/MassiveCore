package com.massivecraft.mcore4.adapter;

import java.lang.reflect.Type;
import java.util.Map.Entry;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.mcore4.xlib.gson.JsonDeserializationContext;
import com.massivecraft.mcore4.xlib.gson.JsonDeserializer;
import com.massivecraft.mcore4.xlib.gson.JsonElement;
import com.massivecraft.mcore4.xlib.gson.JsonObject;
import com.massivecraft.mcore4.xlib.gson.JsonParseException;
import com.massivecraft.mcore4.xlib.gson.JsonSerializationContext;
import com.massivecraft.mcore4.xlib.gson.JsonSerializer;

public class ItemStackAdapter implements JsonDeserializer<ItemStack>, JsonSerializer<ItemStack>
{	
	// -------------------------------------------- //
	// FIELD NAME CONSTANTS
	// -------------------------------------------- //
	
	public static final String TYPE = "type";
	public static final String AMOUNT = "amount";
	public static final String DAMAGE = "damage";
	public static final String ENCHANTMENTS = "enchantments";
	
	// -------------------------------------------- //
	// IMPLEMENTATION
	// -------------------------------------------- //
	
	@Override
	public JsonElement serialize(ItemStack itemStack, Type typeOfSrc, JsonSerializationContext context)
	{
		return toJson(itemStack);
	}
	
	@Override
	public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		return fromJson(json);
	}
	
	// -------------------------------------------- //
	// JSON
	// -------------------------------------------- //
	
	public static JsonObject toJson(ItemStack itemStack)
	{
		if (itemStack == null || itemStack.getTypeId() == 0 || itemStack.getAmount() == 0)
		{
			return null;
		}
		
		JsonObject jsonItemStack = new JsonObject();
		
		jsonItemStack.addProperty(ItemStackAdapter.TYPE, itemStack.getTypeId());
		
		if (itemStack.getAmount() != 1)
		{
			jsonItemStack.addProperty(ItemStackAdapter.AMOUNT, itemStack.getAmount());
		}
		if (itemStack.getDurability() != 0) // Durability is a weird name since it is the amount of damage.
		{
			jsonItemStack.addProperty(ItemStackAdapter.DAMAGE, itemStack.getDurability());
		}
		if (itemStack.getEnchantments().size() > 0)
		{
			JsonObject jsonEnchantments = new JsonObject();
			for (Entry<Enchantment, Integer> entry : itemStack.getEnchantments().entrySet())
			{
				jsonEnchantments.addProperty(String.valueOf(entry.getKey().getId()), entry.getValue());
			}
			jsonItemStack.add(ItemStackAdapter.ENCHANTMENTS, jsonEnchantments);
		}
		return jsonItemStack;
	}
	
	public static ItemStack fromJson(JsonElement json)
	{
		if (json == null || ! json.isJsonObject()) return null;
		
		JsonObject jsonItemStack = json.getAsJsonObject();
		
		// Populate values
		int type = 0; 
		int amount = 1;
		short damage = 0;
		
		if (jsonItemStack.has(ItemStackAdapter.TYPE))
		{
			type = jsonItemStack.get(ItemStackAdapter.TYPE).getAsInt();
		}
		
		if (jsonItemStack.has(ItemStackAdapter.AMOUNT))
		{
			amount = jsonItemStack.get(ItemStackAdapter.AMOUNT).getAsInt();
		}
		
		if (jsonItemStack.has(ItemStackAdapter.DAMAGE))
		{
			damage = jsonItemStack.get(ItemStackAdapter.DAMAGE).getAsShort();
		}
		
		// Create Non enchanted stack
		ItemStack stack = new ItemStack(type, amount, damage);
		
	    // Add enchantments if there are any
		if (jsonItemStack.has(ItemStackAdapter.ENCHANTMENTS))
		{
			JsonObject jsonEnchantments = jsonItemStack.get(ItemStackAdapter.ENCHANTMENTS).getAsJsonObject();
			for (Entry<String, JsonElement> enchantmentEntry: jsonEnchantments.entrySet())
			{
				int enchantmentId = Integer.valueOf(enchantmentEntry.getKey());
				Integer enchantmentLevel = Integer.valueOf(enchantmentEntry.getValue().getAsString());
				stack.addUnsafeEnchantment(Enchantment.getById(enchantmentId), enchantmentLevel);
			}
		}
		
		return stack;
	}
}
