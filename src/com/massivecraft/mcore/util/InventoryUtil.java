package com.massivecraft.mcore.util;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
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
		// Outside inventory?
		if (event.getRawSlot() < 0) return false;
		
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
		// Outside inventory?
		if (event.getRawSlot() < 0) return false;
				
		// Was the upper inventory clicked?
		boolean upperClicked = event.getRawSlot() < event.getInventory().getSize();
		
		boolean ret = false;
		
		if (upperClicked)
		{
			ret = (event.getCurrentItem() != null && event.getCurrentItem().getAmount() > 0);
		}
		
		return ret;
	}
	
	public static boolean isAltering(InventoryClickEvent event)
	{
		return isGiving(event) || isTaking(event);
	}
	
	/**
	 * This method will return the ItemStack the player is trying to equip.
	 * If the click event would not result in equipping something null will be returned.
	 * Note that this algorithm is not perfect. It's an adequate guess.
	 * 
	 * @param event The InventoryClickEvent to analyze.
	 * @return The ItemStack the player is trying to equip.
	 */
	public static ItemStack isEquipping(InventoryClickEvent event)
	{
		/*
		System.out.println("---");
		System.out.println("getInventory().getType() "+event.getInventory().getType());
		System.out.println("getView().getTopInventory().getType() "+event.getView().getTopInventory().getType());
		System.out.println("getView().getType() "+event.getView().getType());
		System.out.println("getView().getBottomInventory().getType() "+event.getView().getBottomInventory().getType());
		System.out.println("event.getSlotType() "+event.getSlotType());
		System.out.println("event.getRawSlot() "+event.getRawSlot());
		System.out.println("event.getSlot() "+event.getSlot());
		*/
		
		boolean isShiftClick = event.isShiftClick();
		InventoryType inventoryType = event.getInventory().getType();
		SlotType slotType = event.getSlotType();
		ItemStack cursor = event.getCursor();
		ItemStack currentItem = event.getCurrentItem();
		
		if (isShiftClick)
		{
			if (inventoryType != InventoryType.CRAFTING) return null;
			if (slotType == SlotType.CRAFTING) return null;
			if (slotType == SlotType.ARMOR) return null;
			if (slotType == SlotType.RESULT) return null;
			if (currentItem.getType() == Material.AIR) return null;
			return currentItem;
		}
		else
		{
			if (slotType == SlotType.ARMOR)
			{
				return cursor;
			}
			return null;
		}
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
	
	// NOTE: This method does not handle the armor part of player inventories.
	// That is expected behavior for now.
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
