package com.massivecraft.massivecore.engine;

import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;
import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.chestgui.ChestAction;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.util.InventoryUtil;

public class EngineMassiveCoreChestGui extends EngineAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineMassiveCoreChestGui i = new EngineMassiveCoreChestGui();
	public static EngineMassiveCoreChestGui get() { return i; }
	
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
		
		// ... warn on bottom inventory ...
		if (InventoryUtil.isBottomInventory(event))
		{
			Mixin.msgOne(event.getWhoClicked(), "<b>Exit the GUI to edit your items.");
			return;
		}
		
		// ... and if this slot index ...
		int index = event.getSlot();
		
		// ... has an action ...
		ChestAction action = gui.getAction(index);
		if (action == null) return;
		
		// ... then play the sound ...
		gui.playSound(event.getWhoClicked());
		
		// ... close the GUI ...
		event.getView().close();
		
		// ... and use that action.
		action.onClick(event);		
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onClose(InventoryCloseEvent event)
	{
		Inventory inventory = event.getInventory();
		if (inventory == null) return;
		ChestGui.remove(inventory);
	}

}
