package com.massivecraft.massivecore.adapter;

import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import com.massivecraft.massivecore.xlib.gson.JsonObject;

public class ItemStackAdapterInner19 extends ItemStackAdapterInner18
{
	// -------------------------------------------- //
	// CONSTANTS: NAMES
	// -------------------------------------------- //
	
	public static final String POTION = "potion";
	
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
		return PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT;
	}
	
	// -------------------------------------------- //
	// SPECIFIC META: POTION
	// -------------------------------------------- //
	
	@Override
	public void transferPotion(ItemStack stack, JsonObject json, boolean meta2json, PotionMeta meta)
	{
		super.transferPotion(stack, json, meta2json, meta);
		
		// TODO: Handle new potions here.
		// TODO: Awaiting the pull request by t00thp1ck.
	}
	
}
