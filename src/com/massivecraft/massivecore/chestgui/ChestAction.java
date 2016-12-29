package com.massivecraft.massivecore.chestgui;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface ChestAction
{
	boolean onClick(InventoryClickEvent event);
}
