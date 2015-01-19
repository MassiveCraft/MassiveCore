package com.massivecraft.massivecore.mixin;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.PlayerInventory;

public interface InventoryMixin
{
	// Create a Player Inventory without a Player
	public PlayerInventory createPlayerInventory();
	
	// Create an arbitrary size standard chest-like inventory
	public Inventory createInventory(InventoryHolder holder, int size, String title);
}
