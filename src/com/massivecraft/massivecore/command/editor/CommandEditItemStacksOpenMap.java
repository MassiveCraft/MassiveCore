package com.massivecraft.massivecore.command.editor;

import java.util.Map;

import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.util.InventoryUtil;

public class CommandEditItemStacksOpenMap<O> extends CommandEditItemStacksOpenAbstract<O, Map<Integer, ItemStack>> implements Listener
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CommandEditItemStacksOpenMap(EditSettings<O> settings, Property<O, Map<Integer, ItemStack>> property)
	{
		// Super
		super(settings, property);
	}
	
	// -------------------------------------------- //
	// CONVERT CONTAINER <--> CHEST
	// -------------------------------------------- //
	
	@Override
	protected Inventory asChestInner(Map<Integer, ItemStack> itemStacks, Inventory ret)
	{
		// Fill Ret
		for (int i = 0; i < 54; i++)
		{
			ItemStack itemStack = itemStacks.get(i);
			if (InventoryUtil.isNothing(itemStack)) continue;
			itemStack = new ItemStack(itemStack);
			
			ret.setItem(i, itemStack);
		}
		
		// Return Ret
		return ret;
	}
	
	protected Map<Integer, ItemStack> asContainer(Inventory inventory)
	{
		// Dodge Null
		if (inventory == null) return null;
		
		// Create Ret
		Map<Integer, ItemStack> ret = new MassiveMap<>();
		
		// Fill Ret
		for (int i = 0; i < inventory.getSize(); i++)
		{
			ItemStack itemStack = inventory.getItem(i);
			if (InventoryUtil.isNothing(itemStack)) continue;
			itemStack = new ItemStack(itemStack);
			
			ret.put(i, itemStack);
		}
		
		// Return Ret
		return ret;
	}
	
}
