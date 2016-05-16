package com.massivecraft.massivecore.mixin;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.PlayerInventory;

import com.massivecraft.massivecore.nms.NmsPlayerInventoryCreate;

public class MixinInventory extends Mixin
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MixinInventory d = new MixinInventory();
	private static MixinInventory i = d;
	public static MixinInventory get() { return i; }
	
	// -------------------------------------------- //
	// METHODS
	// -------------------------------------------- //
	
	public PlayerInventory createPlayerInventory()
	{
		NmsPlayerInventoryCreate nmsPlayerInventory = NmsPlayerInventoryCreate.get();
		if ( ! nmsPlayerInventory.isAvailable()) return null;
		return nmsPlayerInventory.create();
	}
	
	public Inventory createInventory(InventoryHolder holder, int size, String title)
	{
		return Bukkit.createInventory(holder, size, title);
	}

}
