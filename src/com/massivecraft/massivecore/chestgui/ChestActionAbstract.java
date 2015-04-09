package com.massivecraft.massivecore.chestgui;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ChestActionAbstract implements ChestAction
{
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean onClick(InventoryClickEvent event)
	{
		HumanEntity human = event.getWhoClicked();
		if ( ! (human instanceof Player)) return false;
		Player player = (Player)human;
		
		return onClick(event, player);
	}
	
	public boolean onClick(InventoryClickEvent event, Player player)
	{
		return false;
	}
	
}
