package com.massivecraft.massivecore.mixin;

import com.massivecraft.massivecore.nms.NmsRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.Plugin;

public class MixinRecipe extends Mixin
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MixinRecipe d = new MixinRecipe();
	private static MixinRecipe i = d;
	public static MixinRecipe get() { return i; }
	
	// -------------------------------------------- //
	// AVAILABLE
	// -------------------------------------------- //
	
	@Override
	public boolean isAvailable()
	{
		return NmsRecipe.get().isAvailable();
	}
	
	public ShapelessRecipe createShapeless(ItemStack result)
	{
		return NmsRecipe.get().createShapeless(result);
	}
	
	public ShapelessRecipe createShapeless(ItemStack result, Plugin plugin, String key)
	{
		return NmsRecipe.get().createShapeless(result, plugin, key);
	}
	
	public ShapedRecipe createShaped(ItemStack result)
	{
		return NmsRecipe.get().createShaped(result);
	}
	
	public ShapedRecipe createShaped(ItemStack result, Plugin plugin, String key)
	{
		return NmsRecipe.get().createShaped(result, plugin, key);
	}
	
}
