package com.massivecraft.massivecore.nms;

import com.massivecraft.massivecore.mixin.Mixin;
import org.bukkit.inventory.meta.ItemMeta;

public class NmsItemStackMetaUnbreakable extends Mixin
{
	// -------------------------------------------- //
	// DEFAULT
	// -------------------------------------------- //
	
	private static NmsItemStackMetaUnbreakable d = new NmsItemStackMetaUnbreakable().setAlternatives(
		NmsItemStackMetaUnbreakableSpigot.class,
		NmsItemStackMetaUnbreakableFallback.class
	);
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsItemStackMetaUnbreakable i = d;
	public static NmsItemStackMetaUnbreakable get() { return i; }

	// -------------------------------------------- //
	// ACCESS > UNBREAKABLE
	// -------------------------------------------- //
	
	public boolean isUnbreakable(ItemMeta meta)
	{
		throw notImplemented();
	}
	
	public void setUnbreakable(ItemMeta meta, boolean unbreakable) { throw notImplemented(); }
	
}
