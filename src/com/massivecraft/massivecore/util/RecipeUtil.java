package com.massivecraft.massivecore.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

// NOTE: This utility targets 1.9 and will crash on older servers.
public class RecipeUtil
{
	// ------------------------------------------- //
	// POTION
	// -------------------------------------------- //
	
	public static ItemStack createPotionItemStack(PotionType type, Material material, boolean upgraded, boolean extended, int amount)
	{
		ItemStack ret = new ItemStack(material, amount);
		PotionMeta meta = (PotionMeta)ret.getItemMeta();
		PotionData data = new PotionData(type, extended, upgraded);
		meta.setBasePotionData(data);
		ret.setItemMeta(meta);
		return ret;
	}
	
	public static ShapelessRecipe createPotion(PotionType type, Material material, boolean upgraded, boolean extended, Object... objects)
	{
		// When brewing you actually get 3 potions.
		final int amount = 3;
		ItemStack item = createPotionItemStack(type, material, upgraded, extended, amount);
		return createShapeless(item, objects);
	}
	
	public static ShapelessRecipe addPotion(PotionType type, Material material, boolean upgraded, boolean extended, Object... objects)
	{
		ShapelessRecipe recipe = createPotion(type, material, upgraded, extended, objects);
		Bukkit.getServer().addRecipe(recipe);
		return recipe;
	}
	
	// ------------------------------------------- //
	// CIRCULAR
	// -------------------------------------------- //
	
	public static void addCircular(Material material, int maxData)
	{
		ItemStack[] items = new ItemStack[maxData];
		for (int i = 0; i < maxData; i++)
		{
			items[i] = new ItemStack(material, 1, (short) i);
		}
		addCircular(items);
	}
	
	public static void addCircular(ItemStack... items)
	{
		for (int i = 0; i < items.length; i++)
		{
			int next = (i+1) % items.length;
			ItemStack item = items[i];
			addShapeless(items[next], item.getDurability(), item.getAmount(), item.getType());
		}
	}
	
	// ------------------------------------------- //
	// SHAPELESS
	// -------------------------------------------- //
	
	@SuppressWarnings("deprecation")
	public static ShapelessRecipe createShapeless(ItemStack result, Object... objects)
	{
		ShapelessRecipe recipe = new ShapelessRecipe(result);
		
		int quantity = 1;
		int data = 0;
		Material material = null;
		
		for (Object object : objects)
		{
			if (object instanceof Number)
			{
				if (object instanceof Integer)
				{
					quantity = ((Integer)object).intValue();
				}
				else
				{
					data = ((Number)object).intValue();
				}
			}
			else if (object instanceof Material)
			{
				material = (Material)object;
				
				recipe.addIngredient(quantity, material, data);
				
				quantity = 1;
				data = 0;
				material = null;
			}
			else
			{
				throw new IllegalArgumentException(String.valueOf(object));
			}
		}
		
		return recipe;
	}
	
	public static ShapelessRecipe addShapeless(ItemStack result, Object... objects)
	{
		ShapelessRecipe recipe = createShapeless(result, objects);
		Bukkit.getServer().addRecipe(recipe);
		return recipe;
	}
	
}
