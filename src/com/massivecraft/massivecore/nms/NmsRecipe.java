package com.massivecraft.massivecore.nms;

import com.massivecraft.massivecore.mixin.Mixin;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.Plugin;

public class NmsRecipe extends Mixin
{
	// -------------------------------------------- //
	// DEFAULT
	// -------------------------------------------- //
	
	private static NmsRecipe d = new NmsRecipe().setAlternatives(
		NmsRecipe112R1P.class,
		NmsRecipe17R4P.class
	);
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsRecipe i = d;
	public static NmsRecipe get() { return i; }
	
	// -------------------------------------------- //
	// METHODS
	// -------------------------------------------- //
	
	public ShapelessRecipe createShapeless(ItemStack result)
	{
		throw notImplemented();
	}
	
	public ShapelessRecipe createShapeless(ItemStack result, Plugin plugin, String key)
	{
		throw notImplemented();
	}
	
	public ShapedRecipe createShaped(ItemStack result)
	{
		throw notImplemented();
	}
	
	public ShapedRecipe createShaped(ItemStack result, Plugin plugin, String key)
	{
		throw notImplemented();
	}
	
}
