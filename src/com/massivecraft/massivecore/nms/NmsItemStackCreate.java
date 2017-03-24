package com.massivecraft.massivecore.nms;

import com.massivecraft.massivecore.mixin.Mixin;
import org.bukkit.inventory.ItemStack;

public class NmsItemStackCreate extends Mixin
{
	// -------------------------------------------- //
	// DEFAULT
	// -------------------------------------------- //
	
	private static NmsItemStackCreate d = new NmsItemStackCreate().setAlternatives(
		NmsItemStackCreate17R4P.class,
		NmsItemStackCreateFallback.class
	);
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsItemStackCreate i = d;
	public static NmsItemStackCreate get() { return i; }
	
	// -------------------------------------------- //
	// CREATE
	// -------------------------------------------- //
	
	public ItemStack create()
	{
		throw notImplemented();
	}
	
}
