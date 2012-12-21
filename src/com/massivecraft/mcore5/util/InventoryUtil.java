package com.massivecraft.mcore5.util;

import org.bukkit.craftbukkit.v1_4_6.inventory.CraftInventoryCustom;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class InventoryUtil
{
	public static Inventory create(int size)
	{
		return create(null, size, "Chest");
	}
	
	public static Inventory create(InventoryHolder owner, int size)
	{
		return create(owner, size, "Chest");
	}
	
	public static Inventory create(int size, String title)
	{
		return create(null, size, title);
	}
	
	public static Inventory create(InventoryHolder owner, int size, String title)
	{
		return new CraftInventoryCustom(owner, size, title);
	}
}
