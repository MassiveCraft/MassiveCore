package com.massivecraft.mcore3.mongodb;

import java.util.Map.Entry;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.mcore3.lib.mongodb.BasicDBObject;

public class ItemStackAdapter
{	
	// -------------------------------------------- //
	// FIELD NAME CONSTANTS
	// -------------------------------------------- //
	
	public static final String TYPE = "type";
	public static final String AMOUNT = "amount";
	public static final String DAMAGE = "damage";
	public static final String ENCHANTMENTS = "enchantments";
	
	// -------------------------------------------- //
	// STATIC LOGIC
	// -------------------------------------------- //
	
	public static BasicDBObject serialize(ItemStack itemStack)
	{
		if (itemStack == null || itemStack.getTypeId() == 0 || itemStack.getAmount() == 0)
		{
			return null;
		}
		
		BasicDBObject bsonItemStack = new BasicDBObject();
		
		bsonItemStack.put(TYPE, itemStack.getTypeId());
		
		if (itemStack.getAmount() != 1)
		{
			bsonItemStack.put(AMOUNT, itemStack.getAmount());
		}
		if (itemStack.getDurability() != 0) // Durability is a weird name since it is the amount of damage.
		{
			bsonItemStack.put(DAMAGE, itemStack.getDurability());
		}
		if (itemStack.getEnchantments().size() > 0)
		{
			BasicDBObject bsonEnchantments = new BasicDBObject();
			for (Entry<Enchantment, Integer> entry : itemStack.getEnchantments().entrySet())
			{
				bsonEnchantments.put(String.valueOf(entry.getKey().getId()), entry.getValue());
			}
			bsonItemStack.put(ENCHANTMENTS, bsonEnchantments);
		}
		
		return bsonItemStack;
	}
	
	public static ItemStack deserialize(BasicDBObject bsonItemStack)
	{
		if (bsonItemStack == null) return null;
		
		// Populate values
		int type = 0; 
		int amount = 1;
		short damage = 0;
		
		if (bsonItemStack.containsField(TYPE))
		{
			type = bsonItemStack.getInt(TYPE);
		}
		
		if (bsonItemStack.containsField(AMOUNT))
		{
			amount = bsonItemStack.getInt(AMOUNT);
		}
		
		if (bsonItemStack.containsField(DAMAGE))
		{
			damage = (short) bsonItemStack.getInt(DAMAGE);
		}
		
		// Create Non enchanted stack
		ItemStack stack = new ItemStack(type, amount, damage);
		
	    // Add enchantments if there are any
		if (bsonItemStack.containsField(ENCHANTMENTS))
		{
			BasicDBObject bsonEnchantments = (BasicDBObject) bsonItemStack.get(ENCHANTMENTS);
			
			for (Entry<String, Object> enchantmentEntry: bsonEnchantments.entrySet())
			{
				int enchantmentId = Integer.valueOf(enchantmentEntry.getKey());
				Integer enchantmentLevel = (Integer) enchantmentEntry.getValue();
				stack.addUnsafeEnchantment(Enchantment.getById(enchantmentId), enchantmentLevel);
			}
		}
		
		return stack;
	}
}
