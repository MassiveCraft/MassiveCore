package com.massivecraft.massivecore.nms;

import org.bukkit.inventory.meta.ItemMeta;

public class NmsItemStackMetaUnbreakableFallback extends NmsItemStackMetaUnbreakable
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsItemStackMetaUnbreakableFallback i = new NmsItemStackMetaUnbreakableFallback();
	public static NmsItemStackMetaUnbreakableFallback get() { return i; }
	
	// -------------------------------------------- //
	// IS UNBREAKABLE
	// -------------------------------------------- //
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean isUnbreakable(ItemMeta meta) {
		return meta.spigot().isUnbreakable();
	}
	
	// -------------------------------------------- //
	// SET UNBREAKABLE
	// -------------------------------------------- //
	
	@SuppressWarnings("deprecation")
	@Override
	public void setUnbreakable(ItemMeta meta, boolean unbreakable) {
		meta.spigot().setUnbreakable(unbreakable);
	}
	
}
