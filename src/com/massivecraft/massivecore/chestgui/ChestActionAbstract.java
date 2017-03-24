package com.massivecraft.massivecore.chestgui;

import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.MUtil;
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
		Player player = IdUtil.getAsPlayer(event.getWhoClicked());
		if (MUtil.isntPlayer(player)) return false;
		
		return onClick(event, player);
	}
	
	public boolean onClick(InventoryClickEvent event, Player player)
	{
		return false;
	}
	
}
