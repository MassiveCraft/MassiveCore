package com.massivecraft.mcore.util;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R3.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_7_R3.inventory.CraftInventoryPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.inventory.MCorePlayerInventory;

public class InventoryUtil
{
	// -------------------------------------------- //
	// UPDATES
	// -------------------------------------------- //
	
	@SuppressWarnings("deprecation")
	public static void update(HumanEntity human)
	{
		if (!(human instanceof Player)) return;
		Player player = (Player)human;
		player.updateInventory();
	}
	
	public static void updateSoon(final HumanEntity human)
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(MCore.get(), new Runnable()
		{
			@Override
			public void run()
			{
				update(human);
			}
		});
	}
	
	// -------------------------------------------- //
	// EVENT INTERPRETATION
	// -------------------------------------------- //
	
	public static boolean isOutside(InventoryClickEvent event)
	{
		return event.getRawSlot() < 0; 
	}
	public static boolean isTopInventory(InventoryClickEvent event)
	{
		if (isOutside(event)) return false;
		return event.getRawSlot() < event.getInventory().getSize();
	}
	public static boolean isBottomInventory(InventoryClickEvent event)
	{
		if (isOutside(event)) return false;
		return event.getRawSlot() >= event.getInventory().getSize();
	}
	
	public static boolean isGiving(InventoryClickEvent event)
	{
		if (isOutside(event)) return false;
		boolean topClicked = isTopInventory(event);
		
		boolean ret = false;
		
		if (topClicked)
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
		if (isOutside(event)) return false;
		boolean topClicked = isTopInventory(event);
		
		boolean ret = false;
		
		if (topClicked)
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
	// CREATE PLAYER INVENTORY
	// -------------------------------------------- //
	
	public static PlayerInventory createPlayerInventory()
	{
		return new CraftInventoryPlayer(new MCorePlayerInventory());
	}
	
	// -------------------------------------------- //
	// IS EMPTY?
	// -------------------------------------------- //
	
	public static boolean isEmpty(Inventory inv)
	{
		if (inv == null) return true;
		
		for (ItemStack itemStack : inv.getContents())
		{
			if (isSomething(itemStack)) return false;
		}
		
		if (inv instanceof PlayerInventory)
		{
			PlayerInventory pinv = (PlayerInventory)inv;
			
			if (isSomething(pinv.getHelmet())) return false;
			if (isSomething(pinv.getChestplate())) return false;
			if (isSomething(pinv.getLeggings())) return false;
			if (isSomething(pinv.getBoots())) return false;
		}
		
		return true;
	}
	
	public static boolean isNothing(ItemStack itemStack)
	{
		if (itemStack == null) return true;
		if (itemStack.getAmount() == 0) return true;
		if (itemStack.getType() == Material.AIR) return true;
		return false;
	}
	
	public static boolean isSomething(ItemStack itemStack)
	{
		return !isNothing(itemStack);
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
	
	public static Inventory cloneInventory(Inventory inventory)
	{
		if (inventory == null) return null;
		
		Inventory ret = null;
		
		int size = inventory.getSize();
		InventoryHolder holder = inventory.getHolder();
		String title = inventory.getTitle();
		
		if (inventory instanceof PlayerInventory)
		{
			PlayerInventory pret = createPlayerInventory();
			ret = pret;
			
			PlayerInventory pinventory = (PlayerInventory)inventory;
			
			pret.setHelmet(pinventory.getHelmet() == null ? null : new ItemStack(pinventory.getHelmet()));
			pret.setChestplate(pinventory.getChestplate() == null ? null : new ItemStack(pinventory.getChestplate()));
			pret.setLeggings(pinventory.getLeggings() == null ? null : new ItemStack(pinventory.getLeggings()));
			pret.setBoots(pinventory.getBoots() == null ? null : new ItemStack(pinventory.getBoots()));
		}
		else
		{
			ret = new CraftInventoryCustom(holder, size, title);
		}
		
		ItemStack[] contents = cloneItemStacks(inventory.getContents());
		ret.setContents(contents);
		
		return ret;
	}
	
	public static PlayerInventory cloneInventory(PlayerInventory inventory)
	{
		return (PlayerInventory)cloneInventory((Inventory)inventory);
	}
	
	// -------------------------------------------- //
	// EQUALS
	// -------------------------------------------- //

	public static boolean equals(ItemStack one, ItemStack two)
	{
		if (isNothing(one)) return isNothing(two);
		if (isNothing(two)) return false;
		return one.equals(two);
	}
	
	public static boolean equals(ItemStack[] one, ItemStack[] two)
	{
		if (one == null) return two == null;
		if (two == null) return false;
		if (one.length != two.length) return false;
		for (int i = 0; i < one.length; i++)
		{
			if (!equals(one[i], two[i])) return false;
		}
		return true;
	}
	
	public static boolean equals(Inventory one, Inventory two)
	{
		if (one == null) return two == null;
		if (two == null) return false;
		if (!equals(one.getContents(), two.getContents())) return false;
		if (one instanceof PlayerInventory)
		{
			PlayerInventory pone = (PlayerInventory)one;
			if (two instanceof PlayerInventory)
			{
				PlayerInventory ptwo = (PlayerInventory)two;
				return equals(pone.getArmorContents(), ptwo.getArmorContents());
			}
			else
			{
				return false;
			}
		}
		return true;
	}
	
	// -------------------------------------------- //
	// SET CONTENT
	// -------------------------------------------- //
	// This one simply moves the content pointers from on inventory to another.
	// You may want to clone the from inventory first.
	
	public static void setAllContents(Inventory from, Inventory to)
	{
		to.setContents(from.getContents());
		if (from instanceof PlayerInventory)
		{
			PlayerInventory pfrom = (PlayerInventory)from;
			if (to instanceof PlayerInventory)
			{
				PlayerInventory pto = (PlayerInventory)to;
				
				pto.setHelmet(pfrom.getHelmet());
				pto.setChestplate(pfrom.getChestplate());
				pto.setLeggings(pfrom.getLeggings());
				pto.setBoots(pfrom.getBoots());
			}
		}
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
