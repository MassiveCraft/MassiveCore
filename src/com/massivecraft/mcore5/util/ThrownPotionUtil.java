package com.massivecraft.mcore5.util;

import org.bukkit.craftbukkit.v1_4_5.entity.CraftThrownPotion;
import org.bukkit.entity.ThrownPotion;

// PR to add this feature to the API:
// https://github.com/Bukkit/Bukkit/pull/737
public class ThrownPotionUtil
{
	public static int getPotionValue(ThrownPotion potion)
	{
		CraftThrownPotion cpotion = (CraftThrownPotion)potion;
		return cpotion.getHandle().getPotionValue();
	}
}
