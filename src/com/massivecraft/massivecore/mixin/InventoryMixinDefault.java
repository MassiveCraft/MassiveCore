package com.massivecraft.massivecore.mixin;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftInventoryPlayer;

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
		return new CraftInventoryPlayer(new MassiveCorePlayerInventory());
	}
	
	@Override
	public Inventory createInventory(InventoryHolder holder, int size, String title)
	{
		if (title == null)
		{
			title = "Chest";
		}
		return new CraftInventoryCustom(holder, size, title);
	}
	
}