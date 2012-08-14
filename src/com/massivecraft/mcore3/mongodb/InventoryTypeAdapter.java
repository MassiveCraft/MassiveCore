package com.massivecraft.mcore3.mongodb;

import org.bukkit.craftbukkit.inventory.CraftInventoryCustom;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.mcore3.lib.mongodb.BasicDBObject;

public class InventoryTypeAdapter
{
	// -------------------------------------------- //
	// FIELD NAME CONSTANTS
	// -------------------------------------------- //
	
	public static final String SIZE = "size";
	
	// -------------------------------------------- //
	// STATIC LOGIC
	// -------------------------------------------- //
	
	public static BasicDBObject serialize(Inventory src)
	{
		BasicDBObject bsonInventory = new BasicDBObject();
		ItemStack[] itemStacks = src.getContents();
		bsonInventory.put(SIZE, itemStacks.length);
		
		for (int i = 0; i < itemStacks.length; i++)
		{
			ItemStack itemStack = itemStacks[i];
			BasicDBObject bsonItemStack = ItemStackAdapter.serialize(itemStack);
			if (bsonItemStack == null) continue;
			bsonInventory.put(String.valueOf(i), bsonItemStack);
		}
		
		return bsonInventory;
	}
	
	public static Inventory deserialize(BasicDBObject bsonInventory)
	{
		if ( ! bsonInventory.containsField(SIZE)) return null;
		int size = bsonInventory.getInt(SIZE);
		
		ItemStack[] itemStacks = new ItemStack[size];
		
		for (int i = 0; i < size; i++)
		{
			// Fetch the jsonItemStack or mark it as empty and continue
			String stackIdx = String.valueOf(i);
			BasicDBObject bsonItemStack = (BasicDBObject) bsonInventory.get(stackIdx);
			ItemStack itemStack = ItemStackAdapter.deserialize(bsonItemStack);
			itemStacks[i] = itemStack;
		}
		
		Inventory ret = new CraftInventoryCustom(null, size, "items");
		ret.setContents(itemStacks);
		return ret;
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
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