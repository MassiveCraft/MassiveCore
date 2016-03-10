package com.massivecraft.massivecore.adapter;

import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonObject;

public interface ItemStackAdapterInner
{
	public Object provoke();
	
	public JsonObject erialize(ItemStack stack);
	public ItemStack erialize(JsonElement jsonElement);
	
}
