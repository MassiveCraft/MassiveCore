package com.massivecraft.massivecore.nms;

import org.bukkit.inventory.ItemStack;

public class NmsItemStackCreateFallback extends NmsItemStackCreate
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsItemStackCreateFallback i = new NmsItemStackCreateFallback();
	public static NmsItemStackCreateFallback get () { return i; }
	
	// -------------------------------------------- //
	// CREATE
	// -------------------------------------------- //
	
	@SuppressWarnings("deprecation")
	@Override
	public ItemStack create()
	{
		return new ItemStack(0);
	}
	
}
