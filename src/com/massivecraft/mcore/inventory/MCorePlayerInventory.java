package com.massivecraft.mcore.inventory;

import net.minecraft.server.v1_7_R1.EntityHuman;
import net.minecraft.server.v1_7_R1.ItemStack;
import net.minecraft.server.v1_7_R1.PlayerInventory;

import org.bukkit.inventory.InventoryHolder;

/**
 * This is an extended version of the NMS.PlayerInventory.
 * It is extended in such a way that it has a no-arg constructor.
 * It is mainly used for deserialization of PlayerInventor.
 * 
 * There is a link/field to the holder/player.
 * We override internal methods where necessary.
 * 
 * How to update:
 * Make sure there is a no-arg constructor.
 * Search for references to "public EntityHuman player".
 * 
 * As of 1.5.1 these are the references:
 * 
 * a(EntityHuman) (2 matches)
 * g(int)
 * getOwner()
 * k() (2 matches)
 * m() (2 matches)
 * pickup(ItemStack) (2 matches)
 * PlayerInventory(EntityHuman)
 * 
 * As of 1.6.1 and 1.6.2 and 1.6.4 and 1.7.2 these are the references:
 * 
 * a(EntityHuman) (2 matches)
 * a(float)
 * getOwner()
 * k() (2 matches)
 * m() (2 matches)
 * pickup(ItemStack) (2 matches)
 * PlayerInventory(EntityHuman)
 * 
 */

public class MCorePlayerInventory extends PlayerInventory
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public MCorePlayerInventory()
	{
		super(null);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	// Is the entityhuman within reach?
	// Can it edit the inventory content?
	// According to the source code design entityhuman is never null. However we go for safety first.
	@Override
	public boolean a(EntityHuman entityhuman)
	{
		// Null cases
		if (entityhuman == null) return true;
		if (this.player == null) return true;
		
		// Other people can reach at any time
		if (!this.player.equals(entityhuman)) return true;
		
		return super.a(entityhuman);
	}
	
	// This method handles damage dealt to the armor inside the inventory.
	// We simply ignore damage if there is no player.
	@Override
	public void a(float arg0)
	{
		if (this.player == null) return;
		
		super.a(arg0);
	}
	
	// If the player is null there is no owner.
	@Override
	public InventoryHolder getOwner()
	{
		if (this.player == null) return null;
		
		return super.getOwner();
	}
	
	// This method looks like some sort of recurring tick to the items but not inventories.
	// Let's just ignore it if the player is elsewhere.
	@Override
	public void k()
	{
		if (this.player == null) return;
		
		super.k();
	}
	
	// Called when the player dies and no items should be kept.
	// This will cause the player to drop all the items.
	@Override
	public void m()
	{
		if (this.player == null) return;
		
		super.m();
	}
	
	// Pickup the item. Return if the pickup was successful or not.
	@Override
	public boolean pickup(ItemStack arg0)
	{
		if (this.player == null) return false;
		
		return super.pickup(arg0);
	}
	
}
