package com.massivecraft.massivecore.engine;

import org.bukkit.event.Event.Result;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.chestgui.ChestAction;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.util.InventoryUtil;

public class EngineMassiveCoreChestGui extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineMassiveCoreChestGui i = new EngineMassiveCoreChestGui();
	public static EngineMassiveCoreChestGui get() { return i; }
	
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
		
		// ... set last action ...
		gui.setLastAction(action);
		
		// ... then play the sound ...
		gui.getSoundEffect().run(event.getWhoClicked());
		
		// ... close the GUI ...
		event.getView().close();
		
		// ... and use that action.
		action.onClick(event);		
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onOpen(InventoryOpenEvent event)
	{
		// Get
		final Inventory inventory = event.getInventory();
		if (inventory == null) return;
		final ChestGui gui = ChestGui.get(inventory);
		if (gui == null) return;
		
		// Runnables
		Bukkit.getScheduler().runTask(getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				for (Runnable runnable : gui.getRunnablesOpen())
				{
					runnable.run();
				}
			}
		});
	}
	
	// NOTE:
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onClose(InventoryCloseEvent event)
	{
		// Get
		final Inventory inventory = event.getInventory();
		if (inventory == null) return;
		final ChestGui gui = ChestGui.get(inventory);
		if (gui == null) return;
		
		// Runnables
		Bukkit.getScheduler().runTask(getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				for (Runnable runnable : gui.getRunnablesClose())
				{
					runnable.run();
				}
			}
		});
		
		// We save the inventory in the map for a little while.
		// A plugin may want to do something upon the chest gui closing.
		Bukkit.getScheduler().runTaskLater(this.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				ChestGui.remove(inventory);
			}
		}, 20);
	}

}
