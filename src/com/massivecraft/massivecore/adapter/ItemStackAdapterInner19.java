package com.massivecraft.massivecore.adapter;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonObject;

public class ItemStackAdapterInner19 extends ItemStackAdapterInner18
{
	// -------------------------------------------- //
	// CONSTANTS: NAMES
	// -------------------------------------------- //
	
	// TODO: For now we keep erializing with the damage value.
	// public static final String POTION = "potion";
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	public static ItemStackAdapterInner19 i = new ItemStackAdapterInner19();
	public static ItemStackAdapterInner19 get() { return i; }

	// -------------------------------------------- //
	// PROVOKE
	// -------------------------------------------- //
	
	@Override
	public Object provoke()
	{
		throw new RuntimeException("not done coding this yet");
		// return PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT;
	}
	
	// -------------------------------------------- //
	// SPECIFIC META: POTION
	// -------------------------------------------- //
	
	@Override
	public void transferPotion(ItemStack stack, JsonObject json, boolean meta2json, PotionMeta meta)
	{
		super.transferPotion(stack, json, meta2json, meta);
		
		if (meta2json)
		{
			int damage = getPotionDamage(stack, meta);
			if (damage == DEFAULT_DAMAGE)
			{
				json.remove(DAMAGE);
			}
			else
			{
				json.addProperty(DAMAGE, damage);
			}
		}
		else
		{
			int damage = DEFAULT_DAMAGE; 
			JsonElement element = json.get(DAMAGE);
			if (element != null) damage = element.getAsInt();
			setPotionDamage(stack, meta, damage);
		}
	}
	
	// -------------------------------------------- //
	// POTION DAMAGE
	// -------------------------------------------- //
	// Note that there might be potions with invalid damage values.
	// They might have been created by mcmmo and other plugins adding custom potions using custom damage.
	// We do not destroy these invalid potions, but rather keep them as is.
	
	public static int getPotionDamage(ItemStack stack, PotionMeta meta)
	{
		throw new RuntimeException();
	}
	
	public static void setPotionDamage(ItemStack stack, PotionMeta meta, int damage)
	{
		throw new RuntimeException();
	}
	
}
