package com.massivecraft.massivecore.mixin;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.PlayerInventory;

import com.massivecraft.massivecore.nms.NmsInventory;

public class MixinInventory extends MixinAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MixinInventory d = new MixinInventory();
	private static MixinInventory i = d;
	public static MixinInventory get() { return i; }
	public static void set(MixinInventory i) { MixinInventory.i = i; }
	
	// -------------------------------------------- //
	// METHODS
	// -------------------------------------------- //
	
	public PlayerInventory createPlayerInventory()
	{
		return NmsInventory.createPlayerInventory();
	}
	
	public Inventory createInventory(InventoryHolder holder, int size, String title)
	{
		return Bukkit.createInventory(holder, size, title);
	}

}
