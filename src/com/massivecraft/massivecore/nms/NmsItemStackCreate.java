package com.massivecraft.massivecore.nms;

import org.bukkit.inventory.ItemStack;
import com.massivecraft.massivecore.mixin.Mixin;

public class NmsItemStackCreate extends Nms
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
