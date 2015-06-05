package com.massivecraft.massivecore.mixin;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.PlayerInventory;

import com.massivecraft.massivecore.nms.NmsInventory;

public class InventoryMixinDefault extends InventoryMixinAbstract
{	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static InventoryMixinDefault i = new InventoryMixinDefault();
	public static InventoryMixinDefault get() { return i; }

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public PlayerInventory createPlayerInventory()
	{
		return NmsInventory.createPlayerInventory();
	}
	
	@Override
	public Inventory createInventory(InventoryHolder holder, int size, String title)
	{
		return Bukkit.createInventory(holder, size, title);
	}
	
}
