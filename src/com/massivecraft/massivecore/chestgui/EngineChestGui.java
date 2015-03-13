package com.massivecraft.massivecore.chestgui;

import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;
import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.util.InventoryUtil;

public class EngineChestGui extends EngineAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineChestGui i = new EngineChestGui();
	public static EngineChestGui get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Plugin getPlugin()
	{
		return MassiveCore.get();
	}
	
	// -------------------------------------------- //
	// LISTENER
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.LOW)
	public void onClick(InventoryClickEvent event)
	{
		// If this inventory ...
		Inventory inventory = event.getInventory();
		if (inventory == null) return;
		
		// ... is a gui ...
		ChestGui gui = ChestGui.get(inventory);
		if (gui == null) return;
		
		// ... then cancel the event ...
		event.setCancelled(true);
		event.setResult(Result.DENY);
		
		// ... and if there is an item ...
		ItemStack item = event.getCurrentItem();
		if (InventoryUtil.isNothing(item)) return;
		
		// ... and this item has an action ...
		ChestAction action = gui.getAction(item);
		if (action == null) return;
		
		// ... then use that action ...
		action.onClick(event);
		
		// ... play the sound ...
		gui.playSound(event.getWhoClicked());
		
		// ... and close the GUI.
		event.getView().close();
		
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onClose(InventoryCloseEvent event)
	{
		Inventory inventory = event.getInventory();
		if (inventory == null) return;
		ChestGui.remove(inventory);
	}

}
