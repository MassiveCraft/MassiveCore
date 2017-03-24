package com.massivecraft.massivecore.nms;

import com.massivecraft.massivecore.mixin.Mixin;
import org.bukkit.inventory.PlayerInventory;

public class NmsPlayerInventoryCreate extends Mixin
{
	// -------------------------------------------- //
	// DEFAULT
	// -------------------------------------------- //
	
	private static NmsPlayerInventoryCreate d = new NmsPlayerInventoryCreate().setAlternatives(
		NmsPlayerInventoryCreate17R4P.class
	);
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsPlayerInventoryCreate i = d;
	public static NmsPlayerInventoryCreate get() { return i; }
	
	// -------------------------------------------- //
	// CREATE
	// -------------------------------------------- //
	
	public PlayerInventory create()
	{
		throw notImplemented();
	}
	
}
