package com.massivecraft.mcore2.gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.craftbukkit.inventory.CraftInventoryCustom;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.mcore2.lib.gson.JsonDeserializationContext;
import com.massivecraft.mcore2.lib.gson.JsonDeserializer;
import com.massivecraft.mcore2.lib.gson.JsonElement;
import com.massivecraft.mcore2.lib.gson.JsonObject;
import com.massivecraft.mcore2.lib.gson.JsonParseException;
import com.massivecraft.mcore2.lib.gson.JsonPrimitive;
import com.massivecraft.mcore2.lib.gson.JsonSerializationContext;
import com.massivecraft.mcore2.lib.gson.JsonSerializer;

public class InventoryTypeAdapter implements JsonDeserializer<Inventory>, JsonSerializer<Inventory>
{
	private static Logger logger = Logger.getLogger(InventoryTypeAdapter.class.getName());
	private static final String SIZE = "size";
	private static final String TYPE = "type";
	private static final String AMOUNT = "amount";
	private static final String DAMAGE = "damage";
	private static final String ENCHANTMENTS = "enchantments";
	
	@Override
	public Inventory deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{	
		try
		{
			Collection<ItemStack> itemStacks = new ArrayList<ItemStack>();
			
			JsonObject jsonInventory = json.getAsJsonObject();
			int size = jsonInventory.get(SIZE).getAsInt();
			
			for (int i = 0; i < size; i++)
			{
				// Fetch the jsonItemStack or mark it as empty and continue
				String stackIdx = String.valueOf(i);
				if ( ! jsonInventory.has(stackIdx))
				{
					itemStacks.add(null);
					continue;
				}
				JsonObject jsonItemStack = jsonInventory.getAsJsonObject(stackIdx);
				
				// Populate values
				int type = jsonItemStack.get(TYPE).getAsInt();
				int amount = 1;
				short damage = 0;
				
				if (jsonItemStack.has(AMOUNT))
				{
					amount = jsonItemStack.get(AMOUNT).getAsInt();
				}
				
				if (jsonItemStack.has(DAMAGE))
				{
					damage = jsonItemStack.get(DAMAGE).getAsShort();
				}
				
				// Create Non enchanted stack
				ItemStack stack = new ItemStack(type, amount, damage);
				
			    // Add enchantments if there are any
				if (jsonItemStack.has(ENCHANTMENTS))
				{
					JsonObject jsonEnchantments = jsonItemStack.get(ENCHANTMENTS).getAsJsonObject();
					for (Entry<String, JsonElement> enchantmentEntry: jsonEnchantments.entrySet())
					{
						int enchantmentId = Integer.valueOf(enchantmentEntry.getKey());
						Integer enchantmentLevel = Integer.valueOf(enchantmentEntry.getValue().getAsString());
						stack.addEnchantment(Enchantment.getById(enchantmentId), enchantmentLevel);
					}
				}
				itemStacks.add(stack);
			}
			
			
			Inventory ret = new CraftInventoryCustom(null, itemStacks.size(), "items");
			ret.setContents(itemStacks.toArray(new ItemStack[0]));
			return ret;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			logger.warning("Error encountered while deserializing an inventory.");
			return null;
		}
	}

	@Override
	public JsonElement serialize(Inventory src, Type typeOfSrc, JsonSerializationContext context)
	{
		try
		{
			JsonObject jsonInventory = new JsonObject();
			ItemStack[] itemStacks = src.getContents();
			jsonInventory.add(SIZE, new JsonPrimitive(itemStacks.length));
			
			for (int i = 0; i < itemStacks.length; i++)
			{
				ItemStack itemStack = itemStacks[i];
				if (itemStack == null) continue;
				if (itemStack.getTypeId() == 0) continue;
				if (itemStack.getAmount() == 0) continue;
				JsonObject jsonItemStack = new JsonObject();
				
				jsonItemStack.addProperty(TYPE, itemStack.getTypeId());
				if (itemStack.getAmount() != 1)
				{
					jsonItemStack.addProperty(AMOUNT, itemStack.getAmount());
				}
				if (itemStack.getDurability() != 0) // Durability is a weird name since it is the amount of damage.
				{
					jsonItemStack.addProperty(DAMAGE, itemStack.getDurability());
				}
				if (itemStack.getEnchantments().size() > 0)
				{
					JsonObject jsonEnchantments = new JsonObject();
					for (Entry<Enchantment, Integer> entry : itemStack.getEnchantments().entrySet())
					{
						jsonEnchantments.addProperty(String.valueOf(entry.getKey().getId()), entry.getValue());
					}
					jsonItemStack.add(ENCHANTMENTS, jsonEnchantments);
				}
				jsonInventory.add(String.valueOf(i), jsonItemStack);
			}
			return jsonInventory;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			logger.warning("Error encountered while serializing an inventory.");
			return null;
		}
	}
	
	// This utility is nice to have in many cases :)
	public static boolean isInventoryEmpty(Inventory inv)
	{
		if (inv == null) return true;
		for (ItemStack stack : inv.getContents())
		{
			if (stack == null) continue;
			if (stack.getAmount() == 0) continue;
			if (stack.getTypeId() == 0) continue;
			return false;
		}
		return true;
	}
}