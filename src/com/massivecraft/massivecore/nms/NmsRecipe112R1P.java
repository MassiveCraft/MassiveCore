package com.massivecraft.massivecore.nms;

import com.massivecraft.massivecore.MassiveCore;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class NmsRecipe112R1P extends NmsRecipe
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsRecipe112R1P i = new NmsRecipe112R1P();
	public static NmsRecipe112R1P get() { return i; }
	
	// -------------------------------------------- //
	// SETUP
	// -------------------------------------------- //
	
	@Override
	public Object provoke() throws Throwable
	{
		return new NamespacedKey(MassiveCore.get(), "provocation");
	}
	
	// -------------------------------------------- //
	// METHODS
	// -------------------------------------------- //
	
	@Override
	public ShapelessRecipe createShapeless(ItemStack result)
	{
		return createShapeless(result, MassiveCore.get(), UUID.randomUUID().toString());
	}
	
	@Override
	public ShapelessRecipe createShapeless(ItemStack result, Plugin plugin, String key)
	{
		return new ShapelessRecipe(new NamespacedKey(plugin, key), result);
	}
	
	@Override
	public ShapedRecipe createShaped(ItemStack result)
	{
		return createShaped(result, MassiveCore.get(), UUID.randomUUID().toString());
	}
	
	@Override
	public ShapedRecipe createShaped(ItemStack result, Plugin plugin, String key)
	{
		return new ShapedRecipe(new NamespacedKey(plugin, key), result);
	}
	
}
