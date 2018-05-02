package com.massivecraft.massivecore.nms;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.Plugin;

public class NmsRecipe17R4P extends NmsRecipe
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsRecipe17R4P i = new NmsRecipe17R4P();
	public static NmsRecipe17R4P get() { return i; }
	
	// -------------------------------------------- //
	// METHODS
	// -------------------------------------------- //
	
	@Override
	public ShapelessRecipe createShapeless(ItemStack result)
	{
		// Old way of constructing recipes
		return new ShapelessRecipe(result);
	}
	
	@Override
	public ShapelessRecipe createShapeless(ItemStack result, Plugin plugin, String key)
	{
		// This overloaded call is a bit odd backwards from the standard
		// This is because it is for ignoring features not yet present
		return createShapeless(result);
	}
	
	@Override
	public ShapedRecipe createShaped(ItemStack result)
	{
		// Old way of constructing recipes
		return new ShapedRecipe(result);
	}
	
	@Override
	public ShapedRecipe createShaped(ItemStack result, Plugin plugin, String key)
	{
		// This overloaded call is a bit odd backwards from the standard
		// This is because it is for ignoring features not yet present
		return createShaped(result);
	}
	
}
