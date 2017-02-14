package com.massivecraft.massivecore.command.editor;

import java.util.List;

import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.util.InventoryUtil;

public class CommandEditItemStacksOpenList<O> extends CommandEditItemStacksOpenAbstract<O, List<ItemStack>> implements Listener
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CommandEditItemStacksOpenList(EditSettings<O> settings, Property<O, List<ItemStack>> property)
	{
		// Super
		super(settings, property);
	}
	
	// -------------------------------------------- //
	// CONVERT CONTAINER <--> CHEST
	// -------------------------------------------- //
	
	@Override
	protected Inventory asChestInner(List<ItemStack> itemStacks, Inventory ret)
	{
		// Fill Ret
		for (int i = 0; i < itemStacks.size(); i++)
		{
			ItemStack itemStack = itemStacks.get(i);
			if (InventoryUtil.isNothing(itemStack)) continue;
			itemStack = new ItemStack(itemStack);
			
			ret.setItem(i, itemStack);
		}
		
		// Return Ret
		return ret;
	}
	
	@Override
	protected List<ItemStack> asContainer(Inventory inventory)
	{
		// Dodge Null
		if (inventory == null) return null;
		
		// Create Ret
		List<ItemStack> ret = new MassiveList<>();
		
		// Fill Ret
		for (int i = 0; i < inventory.getSize(); i++)
		{
			ItemStack itemStack = inventory.getItem(i);
			if (InventoryUtil.isNothing(itemStack)) continue;
			itemStack = new ItemStack(itemStack);
			
			ret.add(itemStack);
		}
		
		// Return Ret
		return ret;
	}
	
}
