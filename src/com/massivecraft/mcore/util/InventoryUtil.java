package com.massivecraft.mcore.util;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class InventoryUtil
{
	// -------------------------------------------- //
	// EVENT INTERPRETATION
	// -------------------------------------------- //
	
	public static boolean isGiving(InventoryClickEvent event)
	{
		// Was the upper inventory clicked?
		boolean upperClicked = event.getRawSlot() < event.getInventory().getSize();
		
		boolean ret = false;
		
		if (upperClicked)
		{
			ret = (event.getCursor() != null && event.getCursor().getAmount() > 0);
		}
		else
		{
			ret = event.isShiftClick();
		}
		
		return ret;
	}
	
	public static boolean isTaking(InventoryClickEvent event)
	{
		// Was the upper inventory clicked?
		boolean upperClicked = event.getRawSlot() < event.getInventory().getSize();
		
		boolean ret = false;
		
		if (upperClicked)
		{
			ret = (event.getCurrentItem() != null && event.getCurrentItem().getAmount() > 0);
		}
		
		return ret;
	}
	
	// -------------------------------------------- //
	// IS EMPTY?
	// -------------------------------------------- //
	
	public static boolean isEmpty(Inventory inv)
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
	
	// -------------------------------------------- //
	// CLONE ITEMSTACKS/INVENTORY
	// -------------------------------------------- //
	
	public static ItemStack[] cloneItemStacks(ItemStack[] itemStacks)
	{
		ItemStack[] ret = new ItemStack[itemStacks.length];
		for (int i = 0; i < itemStacks.length; i++)
		{
			ItemStack stack = itemStacks[i];
			if (stack == null) continue;
			ret[i] = new ItemStack(itemStacks[i]);
		}
		return ret;
	}
	
	// NOTE: This method may not be compatible with player inventories
	public static Inventory cloneInventory(Inventory inventory)
	{
		if (inventory == null) return null;
		
		InventoryHolder holder = inventory.getHolder();
		int size = inventory.getSize();
		String title = inventory.getTitle();
		ItemStack[] contents = cloneItemStacks(inventory.getContents());
		
		Inventory ret = Bukkit.createInventory(holder, size, title);
		ret.setContents(contents);
		
		return ret;
	}
	
	// -------------------------------------------- //
	// CAN I ADD MANY?
	// -------------------------------------------- //
	
	// Calculate how many times you could add this item to the inventory.
	// NOTE: This method does not alter the inventory.
	public static int roomLeft(Inventory inventory, ItemStack item, int limit)
	{
		inventory = cloneInventory(inventory);
		int ret = 0;
		while(limit <= 0 || ret < limit)
		{
			HashMap<Integer, ItemStack> result = inventory.addItem(item.clone());
			if (result.size() != 0) return ret;
			ret++;
		}
		return ret;
	}
	
	// NOTE: Use the roomLeft method first to ensure this method would succeed
	public static void addItemTimes(Inventory inventory, ItemStack item, int times)
	{
		for (int i = 0 ; i < times ; i++)
		{
			inventory.addItem(item.clone());
		}
	}
	
	// -------------------------------------------- //
	// COUNT
	// -------------------------------------------- //
	
	public static int countSimilar(Inventory inventory, ItemStack itemStack)
	{
		int ret = 0;
		for (ItemStack item : inventory.getContents())
		{
			if (item == null) continue;
			if (!item.isSimilar(itemStack)) continue;
			ret += item.getAmount();
		}
		return ret;
	}

}
